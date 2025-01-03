package com.example.back.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class representing a user in the database.
 * This class is a JPA entity linked to the "users" table in the "bd_pfe" schema.
 */
@Entity
@Table(name = "users", schema = "bd_pfe")
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isAdmin;

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Gets the login.
     *
     * @return the login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the user login.
     *
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Sets the user password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets if the user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", isAdmin=" + isAdmin +
            '}';
    }
}
