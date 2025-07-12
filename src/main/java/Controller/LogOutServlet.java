/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "LogOutServlet", urlPatterns = {"/logout"})
public class LogOutServlet extends HttpServlet {
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 ngày

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Boolean remember = (Boolean) session.getAttribute("remember");
            String email    = (String) session.getAttribute("email");
            String password = (String) session.getAttribute("password");

            if (Boolean.TRUE.equals(remember) && email != null && password != null) {
                // Tạo cookie mới
                Cookie emailCookie = new Cookie("email", URLEncoder.encode(email, "UTF-8"));
                Cookie pwdCookie   = new Cookie("password", URLEncoder.encode(password, "UTF-8"));

                emailCookie.setMaxAge(COOKIE_MAX_AGE);
                pwdCookie.setMaxAge(COOKIE_MAX_AGE);

                response.addCookie(emailCookie);
                response.addCookie(pwdCookie);
            } else {
                // Xóa cookie (nếu tồn tại)
                Cookie emailCookie = new Cookie("email", "");
                Cookie pwdCookie   = new Cookie("password", "");

                emailCookie.setMaxAge(0);
                pwdCookie.setMaxAge(0);

                response.addCookie(emailCookie);
                response.addCookie(pwdCookie);
            }

            session.invalidate();
        }

        // Chuyển về trang login
        response.sendRedirect("login");
    }
}
