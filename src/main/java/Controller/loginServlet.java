/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.User;
import Model.customer;
import Service.loginService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
//                case "facebook": doFacebook(req, resp); break;
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
}
