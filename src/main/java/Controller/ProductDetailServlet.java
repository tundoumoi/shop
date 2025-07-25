package Controller;

import Model.product;
import Model.productVariant;
import Service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product"})
public class ProductDetailServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            product p = productService.getProductById(id);

            if (p == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không tồn tại.");
                return;
            }

            List<productVariant> variants = p.getVariants();
            request.setAttribute("product", p);
            request.setAttribute("variants", p.getVariants());
            
            if (!variants.isEmpty()) {
                productVariant defaultVariant = variants.getFirst();
                request.setAttribute("maxQuantity", defaultVariant.getQuantity());
            }

            request.getRequestDispatcher("PRODUCT/ProductInfo.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Lỗi khi lấy chi tiết sản phẩm.");
        }
    }
}
