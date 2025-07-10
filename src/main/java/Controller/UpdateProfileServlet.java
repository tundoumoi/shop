/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.User;
import Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/update-profile"})
public class UpdateProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService(); // bạn cần có lớp này
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy dữ liệu từ form
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        String address  = request.getParameter("address");

        // Cập nhật thông tin
        currentUser.setFullName(fullName);
        if (password != null && !password.trim().isEmpty()) {
            currentUser.setPassword(password);
        }
        currentUser.setAddress(address);

        // Gọi service để cập nhật vào DB
        boolean updated = userService.updateUser(currentUser);

        if (updated) {
            session.setAttribute("user", currentUser); // cập nhật lại trong session
            response.sendRedirect("edit-profile.jsp?success=true");
        } else {
            request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
            request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
        }
    }
}

