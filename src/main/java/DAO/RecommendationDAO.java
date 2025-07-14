package DAO;

import Model.product;
import Util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class RecommendationDAO {
public List<product> getTopRecommendedProducts(int userId) {
    List<product> list = new ArrayList<>();
    String sql = """
        SELECT TOP 5 p.id
        FROM recommendations r
        JOIN products p ON r.recommended_product_id = p.id
        WHERE r.user_id = ? AND r.score IS NOT NULL
        ORDER BY r.score DESC
    """;

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        productDAO pdao = new productDAO();
        while (rs.next()) {
            int pid = rs.getInt("id");
            product full = pdao.selectProductById(pid);
            if (full != null) {
                list.add(full);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
