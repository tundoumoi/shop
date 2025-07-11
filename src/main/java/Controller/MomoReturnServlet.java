/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import Service.ProductService;
import Util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
/**
 *
 * @author HP
 */


@WebServlet(name = "MomoReturnServlet", urlPatterns = {"/momo-return"})
public class MomoReturnServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String resultCode = request.getParameter("resultCode");

        // Nếu thanh toán thất bại hoặc bị hủy
        if (resultCode == null || !resultCode.equals("0")) {
            response.sendRedirect("thankyou.jsp?resultCode=1006");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        double total = 0;

        for (CartItem item : cart) {
            productVariant variant = productService.getVariantById(item.getVariant().getId());

            if (variant.getQuantity() < item.getQuantity()) {
                request.setAttribute("error", "Sản phẩm '" + item.getProduct().getName()
                        + "' size " + variant.getSize() + " không đủ hàng.");
                request.getRequestDispatcher("cart.jsp").forward(request, response);
                return;
            }

            int newQuantity = variant.getQuantity() - item.getQuantity();
            productService.updateVariantQuantity(variant.getId(), newQuantity);

            total += item.getQuantity() * item.getProduct().getPrice().doubleValue();
        }

        // Soạn email
        StringBuilder body = new StringBuilder();
        body.append("Cảm ơn bạn đã đặt hàng tại United Store!\n\n");
        body.append("Địa chỉ giao hàng:\n").append(user.getAddress()).append("\n\n");
        body.append("Phương thức thanh toán: Ví điện tử Momo\n\n");
        body.append("Chi tiết đơn hàng:\n");

        for (CartItem item : cart) {
            body.append("- ").append(item.getProduct().getName())
                .append(" (Size: ").append(item.getVariant().getSize())
                .append(") x").append(item.getQuantity())
                .append(" = ").append(item.getQuantity() * item.getProduct().getPrice().doubleValue())
                .append(" VND\n");
        }

        body.append("\nTổng cộng: ").append(total).append(" VND");

        // Gửi email
        try {
            EmailUtil.send(user.getEmail(), "Xác nhận đơn hàng - United Store", body.toString());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể gửi email xác nhận đơn hàng.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Xoá giỏ hàng
        session.removeAttribute("cart");

        // Chuyển đến trang cảm ơn
        response.sendRedirect("thankyou.jsp?resultCode=0");
    }
}
