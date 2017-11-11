package com.xinra.reviewcommunity.service;

import com.google.common.collect.Streams;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.entity.PasswordLogin;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.repo.PasswordLoginRepository;
import com.xinra.reviewcommunity.shared.Role;
import com.xinra.reviewcommunity.shared.UserLevel;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
  
  /**
   * Converts a {@link User} entity to an {@link AuthenticatedUserDto}. Note that
   * {@link User#getRoles()} has to be fetched already.
   */
  public static AuthenticatedUserDto getAuthenticatedUserDto(User user, DtoFactory dtoFactory) {
    AuthenticatedUserDto dto = dtoFactory.createDto(AuthenticatedUserDto.class);
    dto.setName(user.getName());
    dto.setPk(user.getPk());
    dto.setRoles(Role.getAllTransitiveRoles(user.getRoles()));
    dto.setPermissions(Role.getAllTransitivePermissions(dto.getRoles()));
    dto.setLevel(UserLevel.getFromRoles(dto.getRoles()));
    return dto;
  }
  
  /**
   * Creates an {@link Authentication} from an {@link AuthenticatedUserDto}.
   */
  public static Authentication getAuthentication(AuthenticatedUserDto dto) {
    // combine roles and permissions so that both can be used with Spring Security
    Set<GrantedAuthority> grantedAuthorities 
        = Streams.concat(dto.getRoles().stream(), dto.getPermissions().stream())
          .map(Object::toString)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
    
    // password is not needed anymore so we omit it
    return new UsernamePasswordAuthenticationToken(dto, null, grantedAuthorities);
  }
  
  /**
   * Creates an {@link Authentication} from a {@link User} entity. Note that {@link User#getRoles()}
   * has to be fetched already.
   */
  public static Authentication getAuthentication(User user, DtoFactory dtoFactory) {
    return getAuthentication(getAuthenticatedUserDto(user, dtoFactory));
  }
  
  private final @NonNull DtoFactory dtoFactory;
  private final @NonNull PasswordLoginRepository<PasswordLogin> passwordLoginRepo;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
    final String username = authentication.getName();
    final String password = authentication.getCredentials().toString();
    
    if (username == null || password == null) {
      throw new BadCredentialsException("Bad Credentials");
    }
    
    PasswordLogin login = passwordLoginRepo.findByUserNameEager(username);
    
    if (login == null || !BCrypt.checkpw(password, login.getPasswordHash())) {
      throw new BadCredentialsException("Bad Credentials");
    }
    
    return getAuthentication(login.getUser(), dtoFactory);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}
