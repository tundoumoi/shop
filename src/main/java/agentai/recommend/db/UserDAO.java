/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<Integer> getAllUserIds() throws Exception {
        List<Integer> list = new ArrayList<>();
        // Query users with role 'user'. We handle both bit and string status if possible, 
        // but typically it's bit in SQL Server.
        String sql = "SELECT id FROM users WHERE role = 'user'";
        // If you want to filter by status, ensure the column exists. 
        // For now, let's keep it simple or check if status is 1.
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        }
        return list;
    }
}