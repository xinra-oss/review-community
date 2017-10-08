package com.xinra.reviewcommunity.service;

import com.google.common.collect.Streams;
import com.xinra.nucleus.service.DtoFactory;
import com.xinra.reviewcommunity.auth.Role;
import com.xinra.reviewcommunity.entity.PasswordLogin;
import com.xinra.reviewcommunity.entity.PasswordLoginRepository;
import com.xinra.reviewcommunity.entity.UserLevel;
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
    
    AuthenticatedUserDto user = dtoFactory.createDto(AuthenticatedUserDto.class);
    user.setName(username);
    user.setPk(login.getUser().getPk());
    user.setRoles(Role.getAllTransitiveRoles(login.getUser().getRoles()));
    user.setPermissions(Role.getAllTransitivePermissions(user.getRoles()));
    user.setLevel(UserLevel.getFromRoles(user.getRoles()));
    
    // combine roles and permissions so that both can be used with Spring Security
    Set<GrantedAuthority> grantedAuthorities 
        = Streams.concat(user.getRoles().stream(), user.getPermissions().stream())
          .map(Object::toString)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toSet());
    
    return new UsernamePasswordAuthenticationToken(user, password, grantedAuthorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}
