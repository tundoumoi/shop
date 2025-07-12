package agentai.recommend.tools;

import agentai.recommend.db.OrderedDAO;
import agentai.recommend.db.ProductDAO;
import agentai.recommend.db.RecommendationDAO;
import agentai.recommend.db.ViewedDAO;
import agentai.recommend.llm.LlmClient;
import static agentai.recommend.llm.LlmClient.calculate;
import agentai.recommend.model.ViewedProduct;
import agentai.recommend.model.OrderedProduct;
import agentai.recommend.model.Product;
import agentai.recommend.model.Recommendation;
import agentai.recommend.model.RecommendationInput;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationCalculator {

    private static final ObjectMapper objectMapper = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
    
    private final ViewedDAO viewedDAO = new ViewedDAO();
    private final OrderedDAO orderedDAO = new OrderedDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final RecommendationDAO recommendationDAO = new RecommendationDAO();

    /**
     * Tính recommend và đồng thời lưu kết quả vào bảng recommendations.
     */
    public List<Recommendation> calculateForUser(int userId) throws Exception {
        // 1) Lấy dữ liệu view và order
        List<ViewedProduct> allViewed = viewedDAO.getByUser(userId);
        List<OrderedProduct> ordered   = orderedDAO.getByUser(userId);

        List<Recommendation> recs = new ArrayList<>();

        if (allViewed.isEmpty()) {
            List<Product> top5 = productDAO.getTopSellingOverall(5);
            
            for (Product p : top5) {
                // Bắt exception ngay trong loop
                float sold = productDAO.getTotalSold(p.getId());
                recs.add(new Recommendation(
                    userId,
                    p.getId(),
                    sold,
                    "top_overall"
                ));
            }
        } else {
            // --- ĐÃ XEM: Chọn 1 sản phẩm xem lâu nhất ---
            ViewedProduct longest = allViewed.stream()
                .max(Comparator.comparing(ViewedProduct::getViewTime))
                .get();
            String category = longest.getCategory();

            // Lấy top-10 sản phẩm bán chạy trong category đó
            List<Product> limitedProducts =
                productDAO.getTopSellingByCategory(category, 10);

            // Đóng gói input và gọi AI
            RecommendationInput input =
                new RecommendationInput(userId, allViewed, ordered, limitedProducts);
            List<Recommendation> allRecs = LlmClient.calculate(input);

            // Sort & cắt còn top-5
            recs = allRecs.stream()
                          .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                          .limit(5)
                          .collect(Collectors.toList());
        }

        // 3) Lưu kết quả vào DB (xóa cũ, insert mới)
        recommendationDAO.saveRecommendations(userId, recs);
        return recs;
    }
    
    public static List<Recommendation> parseRecommendations(JsonNode root, int defaultUserId) {
        List<Recommendation> list = new ArrayList<>();
        // Nếu root là array thì dùng thẳng, còn không thì path vào "recommendations"
        JsonNode arrayNode = root.isArray() ? root : root.path("recommendations");

        for (JsonNode recNode : arrayNode) {
            // Dùng path("userId").asInt(defaultUserId) để fallback nếu key không tồn tại
            int uid       = recNode.path("userId").asInt(defaultUserId);
            int productId = recNode.path("productId").asInt();
            float score   = (float) recNode.path("score").asDouble();
            String algo   = recNode.path("algorithm").asText("default");

            list.add(new Recommendation(uid, productId, score, algo));
        }
        return list;
    }

    
    public static List<Recommendation> calculate_recommendations(String inputJson) 
        throws IOException, NoSuchMethodException {
      // 1) Parse JSON thành RecommendationInput
      RecommendationInput input = objectMapper.readValue(
          inputJson, RecommendationInput.class);
      // 2) Delegate về calculate(Input)
      return calculate(input);
    }
}