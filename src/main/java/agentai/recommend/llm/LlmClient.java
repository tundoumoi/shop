package agentai.recommend.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import agentai.recommend.model.Recommendation;
import agentai.recommend.model.RecommendationInput;
import agentai.recommend.tools.RecommendationCalculator;

import java.io.IOException;
import java.util.List;

public class LlmClient {
    private static final ObjectMapper objectMapper = JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();

    private static final Client GEMINI_CLIENT = Client.builder()
        .apiKey("AIzaSyAeHErFrZSdeSYoEYkr_cCdh8RXalUKF-0")
        .build();

    /**
     * Gửi input tới Gemini, ép trả về JSON ai định dạng, rồi parse thành List<Recommendation>
     */
    public static List<Recommendation> calculate(RecommendationInput input) throws IOException {
        // 1) Serialize input thành JSON
        String inputJson = objectMapper.writeValueAsString(input);

        // 2) System instruction
        Content systemInstruction = Content.fromParts(
            Part.fromText(
                "You are a recommendation engine. " +
                "Output ONLY a JSON array of objects with fields {\"productId\":int, \"score\":float, \"algorithm\":string}." +
                "The \"score\" value must be rounded to at most two decimal places (e.g., 12.34) and must not include unnecessary trailing zeros."
            )
        );

        // 3) Định nghĩa schema cho element của mảng
        Schema itemSchema = Schema.builder()
            .type("object")
            .properties(java.util.Map.of(
                "productId",  Schema.builder().type("integer").build(),
                "score",      Schema.builder().type("number").build(),
                "algorithm",  Schema.builder().type("string").build()
            ))
            .build();

        // 4) Đóng gói thành array schema
        Schema arraySchema = Schema.builder()
            .type("array")
            .items(itemSchema)
            .build();

        // 5) Build config
        GenerateContentConfig config = GenerateContentConfig.builder()
            .systemInstruction(systemInstruction)
            .responseMimeType("application/json")
            .responseSchema(arraySchema)
            .build();

        // 6) Gọi Gemini
        GenerateContentResponse resp = GEMINI_CLIENT.models.generateContent(
            "gemini-2.5-flash",
            inputJson,
            config
        );

        // 7) Lấy raw text, có thể chứa thêm annotation
        String raw = resp.text();
        // 8) Trích đúng phần JSON array
        String jsonOnly = extractJsonArray(raw);
        // 9) Parse JSON thành cây
        JsonNode root = objectMapper.readTree(jsonOnly);

        // 10) Delegate parsing với fallback userId
        return RecommendationCalculator.parseRecommendations(root, input.getUserId());
    }

    /**
     * Tìm substring JSON array trong raw text
     */
    private static String extractJsonArray(String text) {
        int start = text.indexOf('[');
        int end   = text.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        throw new IllegalStateException("Không tìm thấy JSON array trong response: " + text);
    }
}
