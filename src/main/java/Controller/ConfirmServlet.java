package Controller;

import Model.*;
import Service.ProductService;
import Service.UserService;
import Util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
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

        // Lấy email và địa chỉ mới từ form checkout.jsp
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        // ️ Cập nhật tạm thời để dùng cho đơn hàng
        if (email != null && email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            user.setEmail(email);
        }
        if (address != null && !address.trim().isEmpty()) {
            user.setAddress(address);
        }

        //  Lưu lại vào DB nếu bạn muốn (tuỳ chọn)
       new UserService().updateUser(user);
        session.setAttribute("user", user);

        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn phương thức thanh toán.");
            request.getRequestDispatcher("payment.jsp").forward(request, response);
            return;
        }

        double total = 0;

        //  Trừ kho từng sản phẩm
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

        //  Soạn nội dung email xác nhận
        StringBuilder body = new StringBuilder();
        body.append("XÁC NHẬN THÔNG TIN ĐƠN HÀNG\n\n");
        body.append("Khách hàng: ").append(user.getFullName()).append("\n");
        body.append("Email: ").append(user.getEmail()).append("\n");
        body.append("Địa chỉ giao hàng: ").append(user.getAddress()).append("\n\n");

        body.append("Chi tiết đơn hàng:\n");
        for (CartItem item : cart) {
            double lineTotal = item.getQuantity() * item.getProduct().getPrice().doubleValue();
            body.append("- ").append(item.getProduct().getName())
                .append(" (Size: ").append(item.getVariant().getSize())
                .append(") x").append(item.getQuantity())
                .append(" = ").append(lineTotal).append(" VND\n");
        }

        body.append("\nTổng cộng: ").append(total).append(" VND\n");
        body.append("Hình thức thanh toán: ");
        if ("momo".equals(paymentMethod)) {
            body.append("Ví điện tử Momo");
        } else {
            body.append("Thanh toán khi nhận hàng (COD)");
        }

        //  Gửi email
        try {
            EmailUtil.send(user.getEmail(), "Xác nhận đơn hàng - United Store", body.toString());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể gửi email xác nhận đơn hàng.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        //  Xoá giỏ hàng
        session.removeAttribute("cart");

        //  Chuyển đến trang cảm ơn
        response.sendRedirect("thankyou.jsp?resultCode=0");
    }
}
