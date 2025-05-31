/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.registerDAO;
import Model.customer;
import java.sql.SQLException;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class customerService {
    private registerDAO dao = new registerDAO();

    public customer loginEmail(customer c) throws SQLException {
        if (dao.findByEmail(c.getEmail()) != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        dao.insert(c);
        return c;
    }

    public customer loginEmail(String email, String password) throws SQLException {
        customer c = dao.findByEmail(email);
        if (c != null && c.getPassword().equals(password)) return c;
        return null;
    }

    public customer registerGoogle(String googleId, String email, String fullName) throws SQLException {
        // 1. If there's already a user with this Google ID, return it
        customer c = dao.findByGoogleId(googleId);
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
        c = new customer();
        c.setGoogleId(googleId);
        c.setEmail(email);
        c.setFullName(fullName);
        c.setPassword(""); // no password for social login
        dao.insert(c);
        return c;
    }

    public customer registerFacebook(String facebookId, String email, String fullName) throws SQLException {
        // 1. If there's already a user with this Facebook ID, return it
        customer c = dao.findByFacebookId(facebookId);
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
        c = new customer();
        c.setFacebookId(facebookId);
        c.setEmail(email);
        c.setFullName(fullName);
        c.setPassword("");
        dao.insert(c);
        return c;
    }
}
