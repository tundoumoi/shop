/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.customer;
import Util.DatabaseConnection;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class registerDAO {
    private static final String SQL_INSERT = "INSERT INTO users(email, password, full_name, address, google_id, facebook_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SQL_SELECT_GOOGLE = "SELECT * FROM users WHERE google_id = ?";
    private static final String SQL_SELECT_FACEBOOK = "SELECT * FROM users WHERE facebook_id = ?";
    private static final String SQL_UPDATE_GOOGLE = "UPDATE users SET google_id = ? WHERE email = ?";
    private static final String SQL_UPDATE_FACEBOOK = "UPDATE users SET facebook_id = ? WHERE email = ?";

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
        System.out.println("[registerDAO.insert] Bắt đầu insert user: " + c.getEmail());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getEmail());
            ps.setString(2, c.getPassword());
            ps.setString(3, c.getFullName());
            ps.setString(4, c.getAddress()); // ← có thể bị null!
            ps.setString(5, c.getGoogleId());
            ps.setString(6, c.getFacebookId());

            int affected = ps.executeUpdate();
            System.out.println("[registerDAO.insert] Số dòng ảnh hưởng: " + affected);

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                    System.out.println("[registerDAO.insert] Insert thành công. ID: " + c.getId());
                } else {
                    System.out.println("[registerDAO.insert] Không lấy được ID.");
                }
            }

        } catch (SQLException e) {
            System.err.println("[registerDAO.insert] Lỗi SQL: " + e.getMessage());
            throw e;
        }
    }


    public void updateGoogleId(String googleId, String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_GOOGLE)) {
            ps.setString(1, googleId);
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }

    public void updateFacebookId(String facebookId, String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_FACEBOOK)) {
            ps.setString(1, facebookId);
            ps.setString(2, email);
            ps.executeUpdate();
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
