package com.epam.hubarevich.domain;

/**
 * Class used to represent User entity
 * @author Anton_Hubarevich
 * @version 1.0
 */

public class User extends Domain {
    private static final long serialVersionUID = 1L;

    /**
     * User unique identifier
     */
    private Long userId;
    /**
     * user name
     */
    private String userName;
    /**
     * user unique login
     */
    private String userLogin;
    /**
     * user password
     */
    private String userPassword;

    public User() {
    }

    /***
     * user constructor
     * @param userId positive Long value
     * @param userName String value. Limit 50 symbols
     * @param userLogin String value. Limit 30 symbols
     * @param userPassword String value. Limit 30 symbols
     */

    public User(Long userId, String userName, String userLogin, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!userId.equals(user.userId)) {
            return false;
        }
        if (!userName.equals(user.userName)) {
            return false;
        }
        if (!userLogin.equals(user.userLogin)) {
            return false;
        }
        return userPassword.equals(user.userPassword);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + userLogin.hashCode();
        result = 31 * result + userPassword.hashCode();
        return result;
    }
}
