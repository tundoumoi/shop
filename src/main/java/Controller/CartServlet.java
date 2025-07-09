/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author HP
 */

import Model.*;
import Service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        String action = request.getParameter("action");

        // Xử lý xoá sản phẩm khỏi giỏ hàng
        if ("remove".equals(action)) {
            int variantId = Integer.parseInt(request.getParameter("variantId"));
            cart.removeIf(item -> item.getVariant().getId() == variantId);
            session.setAttribute("cart", cart);
            response.sendRedirect("cart.jsp");
            return;
        }

        // Xử lý thêm sản phẩm vào giỏ
        int productId = Integer.parseInt(request.getParameter("productId"));
        int variantId = Integer.parseInt(request.getParameter("variantId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

      ProductService productService = new ProductService();
product p = null;
productVariant v = null;

try {
    p = productService.getProductById(productId);
    v = productService.getVariantById(variantId);
} catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect("error.jsp"); // hoặc bạn có thể forward đến trang lỗi tùy ý
    return;
}

        // Kiểm tra trùng size
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getVariant().getId() == variantId) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        // Nếu chưa có, thêm mới
        if (!found) {
            cart.add(new CartItem(p, v, quantity));
        }

        session.setAttribute("cart", cart);
        response.sendRedirect("cart.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }
}
