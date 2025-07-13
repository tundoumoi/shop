package DAO;

import Model.product;
import Util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class RecommendationDAO {
    public List<product> getTopRecommendedProducts(int userId) {
        List<product> list = new ArrayList<>();
        String sql = """
            SELECT TOP 5 p.*
            FROM recommendations r
            JOIN products p ON r.recommended_product_id = p.id
            WHERE r.user_id = ? AND r.score IS NOT NULL
            ORDER BY r.score DESC
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                product p = new product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                // Nếu cần, bạn có thể bổ sung ảnh chính từ bảng image tại đây
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
