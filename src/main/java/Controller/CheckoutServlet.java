package Controller;

import Model.User;
import Model.CartItem;
import Model.productVariant;
import Service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 1. Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Kiểm tra địa chỉ
        String address = user.getAddress();
        if (address == null || address.trim().isEmpty()) {
            response.sendRedirect("USER/address.jsp");
            return;
        }

        // 3. Kiểm tra giỏ hàng
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Giỏ hàng của bạn đang trống.");
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }

        // 4. Kiểm tra tồn kho
        for (CartItem item : cart) {
            int variantId = item.getVariant().getId();
            int cartQty = item.getQuantity();

            productVariant variant = productService.getVariantById(variantId);
            if (variant.getQuantity() < cartQty) {
                request.setAttribute("error", "Sản phẩm '" + item.getProduct().getName()
                        + "' size " + variant.getSize() + " chỉ còn lại " + variant.getQuantity() + " cái.");
                request.getRequestDispatcher("cart.jsp").forward(request, response);
                return;
            }
        }

        // OK → sang trang xác nhận
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }
}
