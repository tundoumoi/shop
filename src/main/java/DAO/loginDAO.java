/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Model.customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Util.DatabaseConnection;

public class loginDAO {

    public boolean checkLogin(String email, String password) {
        boolean result = false;
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = true;  // tìm thấy user hợp lệ
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Có thể bổ sung thêm: lấy thông tin người dùng sacustomeru khi login thành công
    public customer getUserByEmail(String email) {
        customer user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new customer();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setAddress(rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
