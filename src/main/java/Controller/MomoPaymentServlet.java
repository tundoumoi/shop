package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public class MomoPaymentServlet extends HttpServlet {
    private static final String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
    private static final String partnerCode = "MOMO";
    private static final String accessKey = "F8BBA842ECF85";
    private static final String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
private static final String redirectUrl = "http://localhost:8080/shop/momo-return";
    private static final String ipnUrl = "http://localhost:8080/shop/ipn";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderId = String.valueOf(System.currentTimeMillis());
        String requestId = orderId;
        String amount = request.getParameter("amount");
        String orderInfo = "Thanh toan don hang Momo demo";
        String requestType = "captureWallet";
        String extraData = "";

        response.setContentType("text/plain");

        if (amount == null || amount.isEmpty()) {
            response.getWriter().println("❌ Thiếu tham số amount. Vui lòng truy cập từ form thanh toán.");
            return;
        }

        try {
            // ✅ Tạo rawHash
            String rawHash = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            String signature = hmacSHA256(rawHash, secretKey);

            // ✅ In kiểm tra
            System.out.println("===== DEBUG MOMO REQUEST =====");
            System.out.println("amount = " + amount);
            System.out.println("rawHash = " + rawHash);
            System.out.println("signature = " + signature);

            JSONObject json = new JSONObject();
            json.put("partnerCode", partnerCode);
            json.put("accessKey", accessKey);
            json.put("requestId", requestId);
            json.put("amount", amount);
            json.put("orderId", orderId);
            json.put("orderInfo", orderInfo);
            json.put("redirectUrl", redirectUrl);
            json.put("ipnUrl", ipnUrl);
            json.put("extraData", extraData);
            json.put("requestType", requestType);
            json.put("signature", signature);
            json.put("lang", "vi");

            System.out.println("JSON gửi đi:");
            System.out.println(json.toString(2)); // In đẹp

            // ✅ Gửi request
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("HTTP RESPONSE CODE: " + responseCode);

            InputStream inputStream = (responseCode == 200)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            System.out.println("Phản hồi từ Momo:");
            System.out.println(sb.toString());

            if (responseCode == 200) {
                JSONObject responseJson = new JSONObject(sb.toString());
                String payUrl = responseJson.getString("payUrl");
                response.sendRedirect(payUrl);
            } else {
                response.getWriter().println("❌ Lỗi từ Momo API: " + sb.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("❌ Exception: " + e.getMessage());
        }
    }

    private static String hmacSHA256(String data, String key) throws Exception {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmacSha256.init(secretKeySpec);
        byte[] hash = hmacSha256.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
