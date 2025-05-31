/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.*;
import Model.customer;
import Util.DatabaseConnection;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class customerDAO {
    private static final String SQL_INSERT = "INSERT INTO users(email,password,full_name,address,google_id,facebook_id) VALUES (?,?,?,?,?,?)";
    private static final String SQL_SELECT_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SQL_SELECT_GOOGLE = "SELECT * FROM users WHERE google_id = ?";
    private static final String SQL_SELECT_FACEBOOK = "SELECT * FROM users WHERE facebook_id = ?";
    
    public customer findByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public customer findByGoogleId(String googleId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_GOOGLE)) {
            ps.setString(1, googleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public customer findByFacebookId(String facebookId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_FACEBOOK)) {
            ps.setString(1, facebookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public void insert(customer c) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getEmail());
            ps.setString(2, c.getPassword());
            ps.setString(3, c.getFullName());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getGoogleId());
            ps.setString(6, c.getFacebookId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) c.setId(rs.getInt(1));
        }
    }

    private customer map(ResultSet rs) throws SQLException {
        customer c = new customer();
        c.setId(rs.getInt("id"));
        c.setEmail(rs.getString("email"));
        c.setPassword(rs.getString("password"));
        c.setFullName(rs.getString("full_name"));
        c.setRole(rs.getString("role"));
        c.setGoogleId(rs.getString("google_id"));
        c.setFacebookId(rs.getString("facebook_id"));
        c.setAddress(rs.getString("address"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
