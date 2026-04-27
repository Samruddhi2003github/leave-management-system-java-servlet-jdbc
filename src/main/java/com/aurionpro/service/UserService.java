package com.aurionpro.service;

import com.aurionpro.dao.UserDao;
import com.aurionpro.model.User;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    
    public User validateUser(String username, String password, String role) {
        User user = userDao.validateUser(username, password);
        if (user != null && role.equalsIgnoreCase(user.getRole())) {
            return user;
        }
        return null; 
    }
}
