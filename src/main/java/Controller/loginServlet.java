/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.User;
import Service.loginService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import org.json.JSONObject;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "loginServlet", urlPatterns = {"/login"})
public class loginServlet extends HttpServlet {
    loginService login = new loginService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Chuyển hướng GET đến trang login
        String action = req.getParameter("action");
        if ("login".equals(action) || action == null) {
            req.getRequestDispatcher("LOGIN/Login.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("LOGIN/Login.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "login":    doLogin(req, resp);    break;
//                case "google":   doGoogle(req, resp);   break;
                case "facebook": doFacebook(req, resp); break;
                default:          resp.sendRedirect("LOGIN/Login.jsp");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    // Xử lý đăng nhập email/password
    private void doLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        boolean c = login.login(email, password);
        User cus = new User();
        if (c == true) {
            cus=login.getUserInfo(email);
            HttpSession session = req.getSession();
            session.setAttribute("user", cus);
            if(cus.getRole().equalsIgnoreCase("admin")) resp.sendRedirect("admin.jsp");
            else resp.sendRedirect("products");
        }else {
            resp.sendRedirect("LOGIN/Login.jsp?error=invalid");
        }
    }
    
    private void doFacebook(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accessToken = req.getParameter("access_token");
        if (accessToken == null || accessToken.isEmpty()) {
            resp.sendRedirect("login.jsp?error=fb_token_missing");
            return;
        }

        JSONObject fb = fetchFacebookProfile(accessToken);
        System.out.println(fb.getString("id"));
        User user = login.loginWithFacebook(fb.getString("id"));
        if(user!=null){
            user.setStatus(true);
            req.getSession().setAttribute("user", user);
            if(user.getRole().equalsIgnoreCase("admin")) resp.sendRedirect("admin.jsp");
            else resp.sendRedirect("products");
        }
        else{
            resp.sendRedirect("LOGIN/Login.jsp?error=invalid");
        }
    }
    
    private JSONObject fetchFacebookProfile(String accessToken) throws IOException {
        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String json = in.lines().collect(Collectors.joining());
            return new JSONObject(json);
        }
    }
<<<<<<< Updated upstream
<<<<<<< Updated upstream
}
=======
    
    private void doGoogle(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {
        resp.setContentType("text/html;charset=UTF-8");
        String code = req.getParameter("code");
        String error = req.getParameter("error");
        //neu nguoi dung huy uy quyen
        if(error != null) {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
        GoogleLogin gg = new GoogleLogin();
        String accessToken = gg.getToken(code);
        User acc = gg.getUserInfo(accessToken);

        // 2. Đăng nhập với Google ID
        User user = login.loginWithGoogle(acc.getGoogleId());
        if (user != null) {
            user.setStatus(true);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            // 3. Chuyển trang theo role
            if ("admin".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect("admin.jsp");
            } else {
                resp.sendRedirect("products");
            }
        } else {
            // Nếu user chưa tồn tại → báo lỗi hoặc chuyển hướng
            resp.sendRedirect("LOGIN/Login.jsp?error=google_not_registered");
        }
    }
}
=======
    
    private void doGoogle(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {
        resp.setContentType("text/html;charset=UTF-8");
        String code = req.getParameter("code");
        String error = req.getParameter("error");
        //neu nguoi dung huy uy quyen
        if(error != null) {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
        GoogleLogin gg = new GoogleLogin();
        String accessToken = gg.getToken(code);
        User acc = gg.getUserInfo(accessToken);

        // 2. Đăng nhập với Google ID
        User user = login.loginWithGoogle(acc.getGoogleId());
        if (user != null) {
            user.setStatus(true);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            // 3. Chuyển trang theo role
            if ("admin".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect("admin.jsp");
            } else {
                resp.sendRedirect("products");
            }
        } else {
            // Nếu user chưa tồn tại → báo lỗi hoặc chuyển hướng
            resp.sendRedirect("LOGIN/Login.jsp?error=google_not_registered");
        }
    }
}
>>>>>>> Stashed changes


/*
    1258256292582871
    1258256292582871
<<<<<<< Updated upstream
*/
>>>>>>> Stashed changes
=======
*/
>>>>>>> Stashed changes
