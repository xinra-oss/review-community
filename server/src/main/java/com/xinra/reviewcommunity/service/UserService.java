package com.xinra.reviewcommunity.service;

import com.google.common.collect.ImmutableSet;
import com.xinra.reviewcommunity.auth.Role;
import com.xinra.reviewcommunity.entity.PasswordLogin;
import com.xinra.reviewcommunity.entity.PasswordLoginRepository;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.entity.UserLevel;
import com.xinra.reviewcommunity.entity.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserService extends AbstractService {
  
  public static class UsernameAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }
  
  private @Autowired UserRepository<User> userRepo;
  private @Autowired PasswordLoginRepository<PasswordLogin> passwordLoginRepo;

  private User createUser(String name, String email) {
    
    if (userRepo.getByName(name) != null) {
      throw new UsernameAlreadyExistsException();
    }
    
    User user = entityFactory.createEntity(User.class);
    user.setName(name);
    user.setEmail(email);
    user.setRoles(ImmutableSet.of(Role.USER));
    user.setLevel(UserLevel.USER);
    
    user = userRepo.save(user);
    
    log.info("Created user with name {}", name);
    
    return user;
  }
  
  /**
   * Creates a new user.
   * @param name The username. Must be unique.
   * @param email The email address. May be null.
   * @throws UsernameAlreadyExistsException if there is a user with this name already
   */
  public void createUserWithPassword(@NonNull String name, String email, @NonNull String password) {
    User user = createUser(name, email);
    
    PasswordLogin login = entityFactory.createEntity(PasswordLogin.class);
    login.setUser(user);
    login.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
    
    passwordLoginRepo.save(login);
  }
  
}
