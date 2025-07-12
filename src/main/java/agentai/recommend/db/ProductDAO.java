/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.db;

import agentai.recommend.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LENOVO Ideapad 3
 */
public class ProductDAO {
    public List<Product> getAll() throws SQLException {
        String sql = "SELECT id AS product_id, name, description, price, category FROM products";
        try (var conn = DBConnection.getConnection();
             var ps   = conn.prepareStatement(sql);
             var rs   = ps.executeQuery()) {
          var list = new ArrayList<Product>();
          while (rs.next()) {
            var p = new Product();
            p.setId(rs.getInt("product_id"));
            p.setName(rs.getString("name"));
            p.setDescription(rs.getString("description"));
            p.setPrice(rs.getFloat("price"));
            p.setCategory(rs.getString("category"));
            list.add(p);
          }
          return list;
        }
    }
    
    /** Lấy top N best-sellers toàn bộ */
    public List<Product> getTopSellingOverall(int limit) throws SQLException {
        String sql = """
          SELECT TOP(?) p.id, p.name, p.description, p.price, p.category
          FROM order_items oi
          JOIN product_variants pv ON pv.id = oi.variant_id
          JOIN products p          ON p.id = pv.product_id
          GROUP BY p.id,p.name,p.description,p.price,p.category
          ORDER BY SUM(oi.quantity) DESC
        """;
        return queryProducts(limit, null, sql);
    }

    /** Lấy total sold của một product, để làm score cho trường hợp top_overall */
    public float getTotalSold(int productId) throws SQLException {
        String sql = "SELECT SUM(quantity) FROM order_items WHERE variant_id IN "
                   + "(SELECT id FROM product_variants WHERE product_id=?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
            return 0f;
        }
    }

    /** Lấy top N best-sellers theo category */
    public List<Product> getTopSellingByCategory(String category, int limit) throws SQLException {
        String sql = """
          SELECT TOP(?) p.id, p.name, p.description, p.price, p.category
          FROM order_items oi
          JOIN product_variants pv ON pv.id = oi.variant_id
          JOIN products p          ON p.id = pv.product_id
          WHERE p.category = ?
          GROUP BY p.id,p.name,p.description,p.price,p.category
          ORDER BY SUM(oi.quantity) DESC
        """;
        return queryProducts(limit, category, sql);
    }

    /** Helper chung cho 2 query trên */
    private List<Product> queryProducts(int limit, String category, String sql) throws SQLException {
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            if (category != null) ps.setString(2, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getString("description"),
                  rs.getFloat("price"),
                  rs.getString("category")
                ));
            }
        }
        return list;
    }


}
