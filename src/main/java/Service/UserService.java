/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.UserDAO;
import DAO.registerDAO;
import Model.User;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class UserService {
    private registerDAO dao = new registerDAO();
    private final UserDAO userDao = new UserDAO();

    public User loginEmail(User c) throws SQLException {
        if (dao.findByEmail(c.getEmail()) != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        dao.insert(c);
        return c;
    }

    public User loginEmail(String email, String password) throws SQLException {
        User c = dao.findByEmail(email);
        if (c != null && c.getPassword().equals(password)) return c;
        return null;
    }

    public User registerGoogle(String googleId, String email, String fullName) throws SQLException {
        // 1. If there's already a user with this Google ID, return it
        User c = dao.findByGoogleId(googleId);
        if (c != null) return c;
        
        // 2. If email exists but no Google ID, link the Google account
        c = dao.findByEmail(email);
        if (c != null) {
            if (c.getGoogleId() == null || c.getGoogleId().isEmpty()) {
                dao.updateGoogleId(googleId, email);
                c.setGoogleId(googleId);
            }
            return c;
        }

        // 3. New user: insert
        c = new User();
        c.setGoogleId(googleId);
        c.setEmail(email);
        c.setFullName(fullName);
        c.setPassword(""); // no password for social login
        dao.insert(c);
        return c;
    }

    public User registerFacebook(String facebookId, String email, String fullName) throws SQLException {
        // 1. If there's already a user with this Facebook ID, return it
        User c = dao.findByFacebookId(facebookId);
        if (c != null) return c;
        
        // 2. If email exists but no Facebook ID, link the Facebook account
        c = dao.findByEmail(email);
        if (c != null) {
            if (c.getFacebookId() == null || c.getFacebookId().isEmpty()) {
                dao.updateFacebookId(facebookId, email);
                c.setFacebookId(facebookId);
            }
            return c;
        }

        // 3. New user: insert
        c = new User();
        c.setFacebookId(facebookId);
        c.setEmail(email);
        c.setFullName(fullName);
        c.setPassword("");
        dao.insert(c);
        return c;
    }
    
    /**
     * Creates a new user after validating business rules.
     * @param user the User to create
     * @throws ServiceException if validation fails or database error occurs
     */
    public void createUser(User user) throws ServiceException {
        validateUser(user, false);
        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            throw new ServiceException("Error creating user", e);
        }
    }

    /**
     * Updates an existing user.
     * @param user the User with updated information
     * @throws ServiceException if validation fails or database error occurs
     */
    public void updateUser(User user) throws ServiceException {
        validateUser(user, true);
        try {
            userDao.updateUser(user);
        } catch (SQLException e) {
            throw new ServiceException("Error updating user", e);
        }
    }

    /**
     * Soft-deletes a user by setting status to inactive.
     * @param userId the ID of the user to delete
     * @throws ServiceException if userId is invalid or database error occurs
     */
    public void deleteUser(int userId) throws ServiceException {
        if (userId <= 0) {
            throw new ServiceException("Invalid user ID for deletion");
        }
        try {
            userDao.deleteUser(userId);
        } catch (SQLException e) {
            throw new ServiceException("Error deleting user", e);
        }
    }

    /**
     * Retrieves a user by ID.
     * @param userId the ID of the user
     * @return the User object
     * @throws ServiceException if userId is invalid or database error occurs
     */
    public User getUserById(int userId) throws ServiceException {
        if (userId <= 0) {
            throw new ServiceException("Invalid user ID");
        }
        try {
            User user = userDao.getUserById(userId);
            if (user == null) {
                throw new ServiceException("User not found with ID: " + userId);
            }
            return user;
        } catch (SQLException e) {
            throw new ServiceException("Error retrieving user", e);
        }
    }

    /**
     * Retrieves all active users.
     * @return list of active User objects
     * @throws ServiceException if database error occurs
     */
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDao.getAllUsers();
        } catch (SQLException e) {
            throw new ServiceException("Error retrieving users list", e);
        }
    }

    /**
     * Validates user data.
     * @param user the User to validate
     * @param requireId whether ID must be present (>0)
     * @throws ServiceException if validation fails
     */
    private void validateUser(User user, boolean requireId) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User cannot be null");
        }
        if (requireId && user.getId() <= 0) {
            throw new ServiceException("User ID is required for update");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new ServiceException("Invalid email format");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new ServiceException("Full name is required");
        }
        // Additional business validations can be added here
    }

    /**
     * Custom exception for service-layer errors.
     */
    public static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
