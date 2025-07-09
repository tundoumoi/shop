/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Util.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class revenueDAO {
    /** Lấy doanh thu theo tháng, giữ thứ tự chặt chẽ */
    public Map<String, BigDecimal> getMonthlyRevenue() {
        String sql = """
            SELECT FORMAT(order_date, 'yyyy-MM') AS month,
                   SUM(total_amount) AS total_revenue
            FROM orders
            GROUP BY FORMAT(order_date, 'yyyy-MM')
            ORDER BY month
        """;
        Map<String, BigDecimal> revenues = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                revenues.put(
                  rs.getString("month"),
                  rs.getBigDecimal("total_revenue")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // hoặc ném exception tuỳ yêu cầu
        }
        return revenues;
    }
    
    public Map<String, BigDecimal> getCurrentMonthRevenueByCategory() {
        String sql = """
            SELECT 
              p.category                            AS category,
              SUM(oi.quantity * oi.unit_price)     AS revenue
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN product_variants pv ON oi.variant_id = pv.id
            JOIN products p ON pv.product_id = p.id
            WHERE YEAR(o.order_date)  = YEAR(GETDATE())
              AND MONTH(o.order_date) = MONTH(GETDATE())
            GROUP BY p.category
            ORDER BY p.category
        """;
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(
                  rs.getString("category"),
                  rs.getBigDecimal("revenue")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Có thể throw lên service tuỳ nhu cầu
        }
        return result;
    }
}
