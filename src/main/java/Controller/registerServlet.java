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
import java.util.logging.Logger;


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
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "registerServlet", urlPatterns = {"/register"})
public class registerServlet extends HttpServlet {
    
    private registerService service = new registerService();
    private static final Logger LOGGER = Logger.getLogger(registerServlet.class.getName());
    private static final String GOOGLE_CLIENT_ID = "415815610320-qel0575b0g2stu3iscopdiut6acfej5u.apps.googleusercontent.com";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        String action = req.getParameter("action");
        String provider = req.getParameter("provider");
        String accessToken = req.getParameter("access_token");
        System.out.println("[DEBUG-REGISTER] doPost called: action=" + action
                  + ", provider=" + provider
                  + ", access_token=" + (accessToken != null ? "[PROVIDED]" : "[NULL]"));
        // =================================
        if ("register".equals(action)) {
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
                LOGGER.info("[DEBUG-REGISTER] Facebook registration flow");
                LOGGER.log(Level.FINE, "[DEBUG-REGISTER] Facebook access_token={0}", accessToken);
              JSONObject fb = fetchFacebookProfile(accessToken);
              System.out.println("▶️ [DEBUG-REGISTER] Facebook profile: " + fb.toString());
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
      }
    
    // 1. verifyGoogleToken
    GoogleIdToken.Payload verifyGoogleToken(String idTokenString)
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
            System.out.println("▶️ [DEBUG-FB] fetchFacebookProfile trả về: " + json);
            return new JSONObject(json);
        }
    }
    
    
}