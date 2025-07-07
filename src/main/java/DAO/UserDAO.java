/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.*;
import Model.User;
import Util.DatabaseConnection;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class UserDAO {
    private static final String SQL_INSERT = "INSERT INTO users(email,password,full_name,address,google_id,facebook_id) VALUES (?,?,?,?,?,?)";
    private static final String SQL_SELECT_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String SQL_SELECT_GOOGLE = "SELECT * FROM users WHERE google_id = ?";
    private static final String SQL_SELECT_FACEBOOK = "SELECT * FROM users WHERE facebook_id = ?";
    
    private static final String INSERT_USER_SQL =
        "INSERT INTO users (email, password, full_name, role, facebook_id, google_id, address, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_SQL =
        "UPDATE users SET email = ?, password = ?, full_name = ?, role = ?, " +
        "facebook_id = ?, google_id = ?, address = ?, status = ? WHERE id = ?";
    private static final String SOFT_DELETE_USER_SQL =
        "UPDATE users SET status = 0 WHERE id = ?";
    private static final String SELECT_USER_BY_ID_SQL =
        "SELECT id, email, password, full_name, role, facebook_id, google_id, address, status, created_at " +
        "FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS_SQL =
        "SELECT id, email, password, full_name, role, facebook_id, google_id, address, status, created_at " +
        "FROM users";
    
    public User findByEmail(String email) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public User findByGoogleId(String googleId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_GOOGLE)) {
            ps.setString(1, googleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public User findByFacebookId(String facebookId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_FACEBOOK)) {
            ps.setString(1, facebookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public void insert(User c) throws SQLException {
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

    private User map(ResultSet rs) throws SQLException {
        User c = new User();
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
    
    public void addUser(User user) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getFacebookId());
            ps.setString(6, user.getGoogleId());
            ps.setString(7, user.getAddress());
            ps.setBoolean(8, user.isStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        }
    }

    public void updateUser(User user) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER_SQL)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getFacebookId());
            ps.setString(6, user.getGoogleId());
            ps.setString(7, user.getAddress());
            ps.setBoolean(8, user.isStatus());
            ps.setInt(9, user.getId());
            ps.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SOFT_DELETE_USER_SQL)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public User getUserById(int userId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_ID_SQL)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        }
        return users;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setFacebookId(rs.getString("facebook_id"));
        user.setGoogleId(rs.getString("google_id"));
        user.setAddress(rs.getString("address"));
        user.setStatus(rs.getBoolean("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
