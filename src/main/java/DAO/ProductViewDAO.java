package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Util.DatabaseConnection;

public class ProductViewDAO {

    /**
     * Ghi nhận hoặc cập nhật thời gian xem sản phẩm cho người dùng
     *
     * @param userId ID người dùng
     * @param productId ID sản phẩm
     * @param viewTimeInSeconds Thời gian xem (tính bằng giây)
     */
    public void recordView(int userId, int productId, long viewTimeInSeconds) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Kiểm tra đã có bản ghi với userId + productId chưa
            String checkSQL = "SELECT id, view_time FROM product_views WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, productId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Đã có → cập nhật tổng thời gian
                    int id = rs.getInt("id");
                    long currentTime = rs.getLong("view_time");
                    long newTime = currentTime + viewTimeInSeconds;

                    String updateSQL = "UPDATE product_views SET view_time = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                        updateStmt.setLong(1, newTime);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                    }

                } else {
                    // Chưa có → thêm mới
                    String insertSQL = "INSERT INTO product_views (user_id, product_id, view_time) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setLong(3, viewTimeInSeconds);
                        insertStmt.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
