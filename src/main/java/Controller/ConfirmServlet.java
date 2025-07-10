package Controller;

import Model.*;
import Service.ProductService;
import Util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ConfirmServlet", urlPatterns = {"/confirm"})
public class ConfirmServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("cart.jsp");
            return;
        }

        double total = 0;

        // Trừ kho từng sản phẩm
        for (CartItem item : cart) {
            productVariant variant = productService.getVariantById(item.getVariant().getId());

            if (variant.getQuantity() < item.getQuantity()) {
                request.setAttribute("error", "Sản phẩm '" + item.getProduct().getName()
                        + "' size " + variant.getSize() + " không đủ hàng.");
                request.getRequestDispatcher("cart.jsp").forward(request, response);
                return;
            }

            // Cập nhật tồn kho mới
            int newQuantity = variant.getQuantity() - item.getQuantity();
            productService.updateVariantQuantity(variant.getId(), newQuantity);

            total += item.getQuantity() * item.getProduct().getPrice().doubleValue();
        }

        // Gửi email xác nhận
        StringBuilder body = new StringBuilder();
        body.append("Cảm ơn bạn đã đặt hàng tại United Store!\n\n");
        body.append("Địa chỉ giao hàng:\n").append(user.getAddress()).append("\n\n");
        body.append("Chi tiết đơn hàng:\n");

        for (CartItem item : cart) {
            body.append("- ").append(item.getProduct().getName())
                .append(" (Size: ").append(item.getVariant().getSize())
                .append(") x").append(item.getQuantity())
                .append(" = ").append(item.getQuantity() * item.getProduct().getPrice().doubleValue())
                .append(" VND\n");
        }

        body.append("\nTổng cộng: ").append(total).append(" VND");

        try {
            EmailUtil.send(user.getEmail(), "Xác nhận đơn hàng - United Store", body.toString());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể gửi email xác nhận đơn hàng.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Xoá giỏ hàng sau khi đặt hàng thành công
        session.removeAttribute("cart");

        // Chuyển đến trang cảm ơn
        response.sendRedirect("thankyou.jsp");
    }
}
