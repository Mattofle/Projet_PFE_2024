package com.example.back.user;

/**
 * Class representing a user without credentials.
 * This class is used to store a user without credentials.
 */
public class UserWithoutCred {

  private int userId;
  private String login;
  private boolean isAdmin;

  public UserWithoutCred(User user) {
    this.userId = user.getUserId();
    this.login = user.getLogin();
    this.isAdmin = user.isIsAdmin();
  }

  public boolean isIsAdmin() {
    return isAdmin;
  }

  public int getUserId() {
    return this.userId;
  }

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  @Override
  public String toString() {
    return "UserWithoutCred{" +
        "userId=" + userId +
        ", login='" + login + '\'' +
        ", isAdmin=" + isAdmin +
        '}';
  }
}
