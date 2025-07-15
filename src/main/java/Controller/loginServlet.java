package Controller;

import Model.User;
import Service.loginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

@WebServlet(name = "loginServlet", urlPatterns = {"/login"})
public class loginServlet extends HttpServlet {

    loginService login = new loginService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("login".equals(action) || action == null) {
            req.getRequestDispatcher("LOGIN/Login.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("LOGIN/Login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action) {
                case "login":
                    doLogin(req, resp);
                    break;
//              case "google":
//                  doGoogle(req, resp);
//                  break;
                case "facebook":
                    doFacebook(req, resp);
                    break;
                default:
                    resp.sendRedirect("LOGIN/Login.jsp");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    //  Xử lý đăng nhập email/password
    private void doLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember"); // null nếu không check

        boolean loginSuccess = login.login(email, password);
        if (loginSuccess) {
            User user = login.getUserInfo(email);

            // Lưu session
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("email", email);
            session.setAttribute("password", password);
            session.setAttribute("remember", "on".equals(remember));

            //  Xử lý COOKIE
            if ("on".equals(remember)) {
                // Người dùng chọn Ghi nhớ
                Cookie emailCookie = new Cookie("email", URLEncoder.encode(email, "UTF-8"));
                Cookie passwordCookie = new Cookie("password", URLEncoder.encode(password, "UTF-8"));
                Cookie rememberCookie = new Cookie("remember", "true");

                int maxAge = 7 * 24 * 60 * 60;
                emailCookie.setMaxAge(maxAge);
                passwordCookie.setMaxAge(maxAge);
                rememberCookie.setMaxAge(maxAge);

                emailCookie.setPath("/");
                passwordCookie.setPath("/");
                rememberCookie.setPath("/");

                resp.addCookie(emailCookie);
                resp.addCookie(passwordCookie);
                resp.addCookie(rememberCookie);
            } else {
                //  Không chọn ghi nhớ → xoá cookie
                Cookie emailCookie = new Cookie("email", "");
                Cookie passwordCookie = new Cookie("password", "");
                Cookie rememberCookie = new Cookie("remember", "");

                emailCookie.setMaxAge(0);
                passwordCookie.setMaxAge(0);
                rememberCookie.setMaxAge(0);

                emailCookie.setPath("/");
                passwordCookie.setPath("/");
                rememberCookie.setPath("/");

                resp.addCookie(emailCookie);
                resp.addCookie(passwordCookie);
                resp.addCookie(rememberCookie);
            }

            //  Chuyển hướng theo vai trò
            if ("admin".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect("admin.jsp");
            } else {
                resp.sendRedirect("products");
            }

        } else {
            // Sai tài khoản hoặc mật khẩu
            resp.sendRedirect("LOGIN/Login.jsp?error=invalid");
        }
    }

    //  Xử lý Facebook Login
    private void doFacebook(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accessToken = req.getParameter("access_token");
        if (accessToken == null || accessToken.isEmpty()) {
            resp.sendRedirect("LOGIN/Login.jsp?error=fb_token_missing");
            return;
        }

        JSONObject fb = fetchFacebookProfile(accessToken);
        User user = login.loginWithFacebook(fb.getString("id"));

        if (user != null) {
            user.setStatus(true);
            req.getSession().setAttribute("user", user);

            if ("admin".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect("admin.jsp");
            } else {
                resp.sendRedirect("products");
            }
        } else {
            resp.sendRedirect("LOGIN/Login.jsp?error=invalid");
        }
    }

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
