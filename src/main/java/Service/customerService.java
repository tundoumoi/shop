/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.registerDAO;
import Model.User;
import java.sql.SQLException;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class customerService {
    private registerDAO dao = new registerDAO();

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
}
