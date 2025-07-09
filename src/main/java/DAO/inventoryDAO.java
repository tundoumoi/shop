/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Util.DatabaseConnection;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class inventoryDAO {
    public Map<String, Integer> getFilteredInventory() {
        String sql = """
            WITH SalesCount AS (
                SELECT p.id            AS product_id,
                       SUM(oi.quantity) AS total_sold
                FROM order_items oi
                JOIN product_variants pv ON oi.variant_id  = pv.id
                JOIN products p          ON pv.product_id = p.id
                GROUP BY p.id
            ), LeastSold AS (
                SELECT TOP 20 product_id
                FROM SalesCount
                ORDER BY total_sold ASC
            )
            SELECT
              p.name           AS product_name,
              SUM(pv.quantity) AS stock_quantity
            FROM product_variants pv
            JOIN products p
              ON pv.product_id = p.id
            WHERE p.status = 0                     -- sản phẩm tạm ẩn
               OR p.id IN (SELECT product_id FROM LeastSold)
            GROUP BY p.name
            ORDER BY stock_quantity ASC
        """;

        Map<String, Integer> inventory = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                inventory.put(
                  rs.getString("product_name"),
                  rs.getInt("stock_quantity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Bạn có thể ném exception tuỳ nhu cầu
        }
        return inventory;
    }
}
