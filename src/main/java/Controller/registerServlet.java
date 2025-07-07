/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import Service.registerService;
import Service.registerService.DuplicateAuthException;

// Google OAuth
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpSession;

// Facebook HTTP
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "registerServlet", urlPatterns = {"/register"})
public class registerServlet extends HttpServlet {
    
    private registerService service = new registerService();
    private static final String GOOGLE_CLIENT_ID = "415815610320-qel0575b0g2stu3iscopdiut6acfej5u.apps.googleusercontent.com";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("register".equals(action)) {
          String provider = req.getParameter("provider");
          try {
            User c = null;
            if ("google".equals(provider)) {
              String idToken = req.getParameter("id_token");
              GoogleIdToken.Payload payload = verifyGoogleToken(idToken);
              c = service.registerGoogle(
                  payload.getSubject(), 
                  payload.getEmail(), 
                  (String) payload.get("name")
              );
            }
            else if ("facebook".equals(provider)) {
              String accessToken = req.getParameter("access_token");
              JSONObject fb = fetchFacebookProfile(accessToken);
              c = service.registerFacebook(
                  fb.getString("id"), 
                  fb.optString("email"), 
                  fb.getString("name")
              );
            }
            else {
              // Đăng ký bằng email/password
              User input = new User(
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("fullName"),
                req.getParameter("address")
              );
              c = service.registerEmail(input);
            }

            // Thành công → lưu session + chuyển trang
            req.getSession().setAttribute("user", c);
            resp.sendRedirect("products");
          }
          catch (DuplicateAuthException dae) {
            // nếu trùng đăng ký social hoặc email đã tồn tại → báo lỗi và quay lại form
            req.setAttribute("error", dae.getMessage());
            req.getRequestDispatcher("/Login.jsp").forward(req, resp);
          }
          catch (Exception e) {
            throw new ServletException(e);
          }
        }
        // ... có thể có login/update khác
      }
    
    // 1. verifyGoogleToken
    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString)
            throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
            )
            .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
            .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new SecurityException("Invalid Google ID token");
        }
    }
    
    // 2. fetchFacebookProfile
    private JSONObject fetchFacebookProfile(String accessToken) throws IOException {
        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String json = in.lines().collect(Collectors.joining());
            return new JSONObject(json);
        }
    }
    
    
}

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter("action");
//        try {
//            switch (action) {
//                case "google": doGoogle(req, resp); break;
//                case "facebook": doFacebook(req, resp); break;
//                // other cases...
//                default: resp.sendRedirect("login.jsp");
//            }
//        } catch (Exception e) {
//            throw new ServletException(e);
//        }
//    }
//
//    private void doGoogle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//        String idTokenString = req.getParameter("id_token");
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JSON_FACTORY)
//            .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
//            .build();
//
//        GoogleIdToken idToken = verifier.verify(idTokenString);
//        if (idToken != null) {
//            var payload = idToken.getPayload();
//            customer c = service.registerGoogle(
//                payload.getSubject(),
//                payload.getEmail(),
//                (String) payload.get("name")
//            );
//            HttpSession session = req.getSession();
//            session.setAttribute("user", c);
//            resp.sendRedirect("customer.jsp");
//        } else {
//            resp.sendRedirect("login.jsp?error=invalid_token");
//        }
//    }
//
//    private void doFacebook(HttpServletRequest req, HttpServletResponse resp) throws Exception {
//        String token = req.getParameter("access_token");
//        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + token;
//        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//        conn.setRequestMethod("GET");
//        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = in.readLine()) != null) sb.append(line);
//        in.close();
//        JSONObject obj = new JSONObject(sb.toString());
//
//        customer c = service.registerFacebook(
//            obj.getString("id"), obj.optString("email"), obj.getString("name")
//        );
//        HttpSession session = req.getSession();
//        session.setAttribute("user", c);
//        resp.sendRedirect("customer.jsp");
//    }
//   
//}
/**
 1. customer đăng kí bằng facebook hoặc google
 2. Lấy dữ liệu đăng kí kiểm tra trong database nếu chưa có thì thêm vào bảng user và ID : xxxx(xxxx là số và tự động tăng dần),
 nếu đã có trong databáe thì thì kiểm tra phương thức đăng kí đã có chưa nếu chưa thì cập nhật vào ID đó , nếu đã có phhuonwg thức
 đăng kí rồi thì kiểm tra có bị trùng không nếu bị trùng thì quay lại trang Login.jsp, nếu không trùng thì thêm mới một customer và
 chuyển hướng và dữ kiệu dữ liệu của customer đó sang trang customer.
 * 
 */