/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.db;


import agentai.recommend.model.ViewedProduct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewedDAO {
    public List<ViewedProduct> getByUser(int userId) throws SQLException {
        String sql = "SELECT " +
                    "    p.id, " +
                    "    p.name, " +
                    "    p.description, " +
                    "    p.price, " +
                    "    p.category, " +
                    "    NULL AS size, " +
                    "    pv.view_time " +
                    "FROM product_views pv " +
                    "JOIN products p ON pv.product_id = p.id " +
                    "WHERE pv.user_id = ?" +
                    "ORDER BY pv.view_time DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<ViewedProduct> list = new ArrayList<>();
                while (rs.next()) {
                    java.math.BigDecimal viewTime;
                    Object vtObj = rs.getObject("view_time");
                    if (vtObj instanceof java.sql.Timestamp) {
                        viewTime = java.math.BigDecimal.valueOf(((java.sql.Timestamp) vtObj).getTime());
                    } else if (vtObj instanceof java.math.BigDecimal) {
                        viewTime = (java.math.BigDecimal) vtObj;
                    } else if (vtObj instanceof Number) {
                        viewTime = java.math.BigDecimal.valueOf(((Number) vtObj).doubleValue());
                    } else {
                        viewTime = java.math.BigDecimal.ZERO;
                    }

                    ViewedProduct vp = new ViewedProduct(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getString("category"),
                        rs.getString("size"),
                        viewTime
                    );
                    list.add(vp);
                }
                return list;
            }
        }
    }
}

