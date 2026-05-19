package agentai.recommend.agents;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import agentai.recommend.db.RecommendationDAO;
import agentai.recommend.db.UserDAO;
import agentai.recommend.model.Recommendation;
import agentai.recommend.tools.RecommendationCalculator;

/**
 * Entry point để chạy định kỳ hệ thống recommendation cho tất cả users.
 */
public class RecommendationAgent {
    // Sử dụng ScheduledExecutorService để lập lịch chạy định kỳ
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor();
    private static final RecommendationCalculator recommend = new RecommendationCalculator();

    public static void main(String[] args) {
        // Chạy ngay khi khởi động
        System.out.println("đang chạy Agent");
        runAllUsers();
        // Lên lịch chạy lại mỗi 24 giờ
        SCHEDULER.scheduleAtFixedRate(
                RecommendationAgent::runAllUsers,
                24,   // Delay ban đầu: 24 giờ
                24,   // Khoảng thời gian giữa các lần chạy: 24 giờ
                TimeUnit.HOURS
        );
    }

    /**
     * Quét tất cả userId, tính recommendation, xóa cũ và lưu mới.
     */
    public static void runAllUsers() {
        System.out.println("Starting recommendation for all users...");
        try {
            UserDAO userDao = new UserDAO();
            RecommendationDAO recDao = new RecommendationDAO();
            List<Integer> userIds = userDao.getAllUserIds();
            System.out.println("Found " + userIds.size() + " users to process.");
            for (int userId : userIds) {
                try {
                    System.out.println("Processing recommendation for userId: " + userId);
                    // Tính recommendation
                    List<Recommendation> recs = recommend.calculateForUser(userId);
                    System.out.println("Generated " + (recs != null ? recs.size() : 0) + " recommendations for userId: " + userId);
                    // Lưu batch recommendation mới (nếu calculateForUser chưa lưu)
                    // recDao.saveRecommendations(userId, recs); // calculateForUser đã gọi cái này
                } catch (Exception e) {
                    // Log lỗi cho từng user nhưng tiếp tục với user khác
                    System.err.println("Error processing user " + userId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Finished recommendation for all users.");
        } catch (Exception e) {
            // Log lỗi chung
            System.err.println("Failed to run recommendation for all users: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
