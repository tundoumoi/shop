/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filter;

import java.io.IOException;
import java.util.*;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import Model.User;
/**
 *
 * @author LENOVO Ideapad 3
 */
@WebFilter(filterName = "AuthenFilter", urlPatterns = {"/*"})
public class AuthenFilter implements Filter{
    private static final String US = "user";
    private static final String AD = "admin";
    private static final String LOGIN_PAGE = "LOGIN/Login.jsp";
    private static final boolean DEBUG = true;

    private static final Set<String> ADMIN_FUNC = new HashSet<>();
    private static final Set<String> USER_FUNC = new HashSet<>();

    private static final Set<String> STATIC_RESOURCES = new HashSet<>(Arrays.asList(
            ".css", ".js", ".jpg", ".jpeg", ".png", ".gif", ".woff", ".svg"
    ));

    private FilterConfig filterConfig = null;

    public AuthenFilter(){
        // static & login
        ADMIN_FUNC.add("Login.jsp");
        ADMIN_FUNC.add("login.css");
        USER_FUNC.add("Login.jsp");
        USER_FUNC.add("login.css");

        // Admin
        ADMIN_FUNC.add("users");         // UserServlet
        ADMIN_FUNC.add("listUser.jsp");
        ADMIN_FUNC.add("createUser.jsp");
        ADMIN_FUNC.add("editUser.jsp");
        
        ADMIN_FUNC.add("register");         // RegisterServlet
        

        ADMIN_FUNC.add("products");      // ProductServlet
        ADMIN_FUNC.add("ListProductForAdmin.jsp");
        ADMIN_FUNC.add("CreateProduct.jsp");
        ADMIN_FUNC.add("EditProduct.jsp");

        ADMIN_FUNC.add("revenues");      // RevenueServlet
        ADMIN_FUNC.add("revenue.jsp");
        
        ADMIN_FUNC.add("login");
        ADMIN_FUNC.add("logout");
        ADMIN_FUNC.add("resetPassword");
        ADMIN_FUNC.add("requestPassword");
        ADMIN_FUNC.add("runrecommendationagent");
        
        ADMIN_FUNC.add("admin.jsp");
        ADMIN_FUNC.add("requestPassword.jsp");
        ADMIN_FUNC.add("resetPassword.jsp");
        
        USER_FUNC.add("products");       // ProductServlet
        USER_FUNC.add("product");        // ProductDetailServlet
        USER_FUNC.add("ProductList.jsp");
        USER_FUNC.add("ProductInfor.jsp");
        USER_FUNC.add("cart.jsp");
        USER_FUNC.add("checkout.jsp");
        USER_FUNC.add("searchResults.jsp");

        USER_FUNC.add("update-profile"); // UpdateProfileServlet
        USER_FUNC.add("edit-profile.jsp");

        USER_FUNC.add("confirm");        // ConfirmServlet
        USER_FUNC.add("momo-return");    // MomoReturnServlet
        USER_FUNC.add("payment.jsp");

        USER_FUNC.add("register");       
        USER_FUNC.add("thankyou.jsp");
        USER_FUNC.add("header.jsp");
        USER_FUNC.add("footer.jsp");
        USER_FUNC.add("resetPassword.jsp");
        USER_FUNC.add("requestPassword.jsp");

        USER_FUNC.add("search");         
        USER_FUNC.add("MomoPaymentServlet");
        USER_FUNC.add("cart");
        USER_FUNC.add("checkout");
        USER_FUNC.add("resetPassword");
        USER_FUNC.add("requestPassword");

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if (DEBUG) {
            System.out.println("AuthenFilter initialized.");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String uri = req.getRequestURI();
            // Cho phép truy cập các tài nguyên tĩnh và trang login
            if (isStaticResource(uri) || uri.endsWith(LOGIN_PAGE) || uri.endsWith("/login") || uri.endsWith("/register") || uri.endsWith("/resetPassword") || uri.endsWith("/requestPassword")) {
                chain.doFilter(request, response);
                return;
            }
            // Lấy tên tài nguyên
            int index = uri.lastIndexOf("/");
            String resource = uri.substring(index + 1);

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                res.sendRedirect(LOGIN_PAGE);
                return;
            }

            User user = (User) session.getAttribute("user");
            String role = user.getRole().toLowerCase();

            // Phân quyền truy cập theo role
            if (US.equals(role) && USER_FUNC.contains(resource)) {
                chain.doFilter(request, response);
            } else if (AD.equals(role) && ADMIN_FUNC.contains(resource)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(LOGIN_PAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Nên dùng logger thực tế
            ((HttpServletResponse) response).sendRedirect(LOGIN_PAGE);
        }
    }

    @Override
    public void destroy() {
        
    }

    private boolean isStaticResource(String uri) {
        for (String ext : STATIC_RESOURCES) {
            if (uri.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
