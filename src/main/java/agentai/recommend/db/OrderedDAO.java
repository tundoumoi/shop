/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.db;

import agentai.recommend.model.OrderedProduct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderedDAO {
    public List<OrderedProduct> getByUser(int userId) throws SQLException {
    String sql =
        "SELECT "
      + "  pv.product_id AS product_id, "
      + "  p.name, "
      + "  p.description, "
      + "  oi.unit_price, "
      + "  p.category, "
      + "  pv.size, "
      + "  oi.quantity "
      + "FROM orders o "
      + "JOIN order_items oi ON o.id = oi.order_id "
      + "JOIN product_variants pv ON oi.variant_id = pv.id "
      + "JOIN products p ON pv.product_id = p.id "
      + "WHERE o.user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        List<OrderedProduct> list = new ArrayList<>();
        while (rs.next()) {
            OrderedProduct op = new OrderedProduct();
            op.setId(rs.getInt("product_id"));           // lấy từ pv.product_id
            op.setName(rs.getString("name"));
            op.setDescription(rs.getString("description"));
            op.setUnitPrice(rs.getBigDecimal("unit_price"));  // hoặc getBigDecimal nếu bạn đổi field thành BigDecimal
            op.setCategory(rs.getString("category"));
            op.setSize(rs.getString("size"));
            op.setQuantity(rs.getInt("quantity"));
            list.add(op);
        }
        return list;
    }
}

}
