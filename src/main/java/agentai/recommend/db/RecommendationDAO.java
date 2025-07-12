package agentai.recommend.db;

import agentai.recommend.model.Recommendation;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecommendationDAO {

    public void deleteByUser(int userId) throws SQLException {
        String sql = "DELETE FROM recommendations WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public void insertBatch(int userId, List<Recommendation> recs) throws SQLException {
        String sql = "INSERT INTO recommendations (user_id, recommended_product_id, score, algorithm) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Recommendation r : recs) {
                ps.setInt(1, userId);
                ps.setInt(2, r.getProductId());
                ps.setFloat(3, r.getScore());
                ps.setString(4, r.getAlgorithm());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    public void saveRecommendations(int userId, List<Recommendation> recs) throws SQLException {
        String deleteSql = "DELETE FROM recommendations WHERE user_id = ?";
        String insertSql = "INSERT INTO recommendations(user_id, recommended_product_id, score, algorithm) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement del = conn.prepareStatement(deleteSql);
             PreparedStatement ins = conn.prepareStatement(insertSql)) {

            // Xóa hết
            del.setInt(1, userId);
            del.executeUpdate();

            // Insert batch
            for (Recommendation r : recs) {
                ins.setInt   (1, userId);
                ins.setInt   (2, r.getProductId());
                ins.setFloat (3, r.getScore());
                ins.setString(4, r.getAlgorithm());
                ins.addBatch();
            }
            ins.executeBatch();
        }
    }
}
