package com.example.back.user;

import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service class for managing users.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to users, using the UserRepository for database interactions.
 */
@Service
public class UserService {

  UserRepository userRepository;

  /**
   * Constructor for the UserService.
   * @param userRepository the user repository
   */
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * get a user by login.
   * @param login the user's login
   * @return the user
   */
  public User getUserByLogin(String login) {
    return userRepository.findByLogin(login);
  }

  /**
   * get a user by id.
   * @param userId the user's id
   * @return the user
   */
  public User getUserById(int userId) {
    return userRepository.findByUserId(userId);
  }

  /**
   * get all users.
   * @return an iterable of users
   */
  public Iterable<UserWithoutCred> getUsers() {
    List<UserWithoutCred> list = new ArrayList<>();

    for (User user : userRepository.findAll()) {
      list.add(new UserWithoutCred(user));
    }
    return list;
  }

  /**
   * get all admins.
   * @return an iterable of admins
   */
  public Iterable<UserWithoutCred> getAdmins() {
    Iterable<UserWithoutCred> allUsers = getUsers();

    return StreamSupport.stream(allUsers.spliterator(), false)
            .filter(UserWithoutCred::isIsAdmin)
            .collect(Collectors.toList());
  }

  /**
   * delete a user.
   * @return if the user was deleted
   */
  public boolean deleteUser(int userId) {
    User user = userRepository.findByUserId(userId);
    if(user != null && user.isIsAdmin()) {
      userRepository.delete(user);
      return true;
    }
    return false;
  }


  /**
   * modify a user's password.
   * @param userId the user's id
   * @param password the new password
   * @return the modified user
   */
  public User modifyPassword(int userId, String password) {
    User user = userRepository.findByUserId(userId);
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    user.setPassword(hashedPassword);
    return userRepository.save(user);
  }

  /**
   * check if a password is correct.
   * @param userId the user's id
   * @param password the password
   * @return if the password is correct
   */
  public Boolean checkPassword(int userId, String password) {
    User user = userRepository.findByUserId(userId);
    return BCrypt.checkpw(password, user.getPassword());
  }

}
