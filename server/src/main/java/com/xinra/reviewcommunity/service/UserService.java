package com.xinra.reviewcommunity.service;

import com.google.common.collect.ImmutableSet;
import com.xinra.reviewcommunity.auth.Role;
import com.xinra.reviewcommunity.entity.PasswordLogin;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.entity.UserLevel;
import com.xinra.reviewcommunity.repo.PasswordLoginRepository;
import com.xinra.reviewcommunity.repo.UserRepository;
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
    
    public UsernameAlreadyExistsException(String username) {
      super("A user with name '" + username + "' already exists!");
    }
  }
  
  public static class UsernameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public UsernameNotFoundException(String username) {
      super("A user with name '" + username + "' does not exist!");
    }
  }
  
  private @Autowired UserRepository<User> userRepo;
  private @Autowired PasswordLoginRepository<PasswordLogin> passwordLoginRepo;

  private User createUser(String name, String email) {
    
    if (userRepo.findByName(name) != null) {
      throw new UsernameAlreadyExistsException(name);
    }
    
    User user = entityFactory.createEntity(User.class);
    user.setName(name);
    user.setEmail(email);
    user.setRoles(ImmutableSet.of(Role.USER));
    updateLevel(user);
    
    user = userRepo.save(user);
    
    log.info("Created user with name '{}'", name);
    
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
  
  /**
   * Adds a role to the user with the given name.
   * @throws UsernameNotFoundException if there is no user with the given name
   */
  public void addRole(@NonNull String username, @NonNull Role role) {
    User user = loadByName(username);
    user.getRoles().add(role);
    updateLevel(user);
    log.info("Add role {} to user with name '{}'. New level is {}", role, username,
        user.getLevel());
    userRepo.save(user);
  }
  
  /**
   * Loads the user with the given name from the database.
   * @throws UsernameNotFoundException if there is no user with the given name
   */
  private User loadByName(String name) {
    User user = userRepo.findByName(name);
    if (user == null) {
      throw new UsernameNotFoundException(name);
    }
    return user;
  }
  
  /**
   * Updates the {@link UserLevel} of the given user. Does not save to database.
   */
  private void updateLevel(User user) {
    user.setLevel(UserLevel.getFromRoles(user.getRoles()));
  }
  
}
