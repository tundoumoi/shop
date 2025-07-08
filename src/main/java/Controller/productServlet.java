/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.product;
import Model.productImage;
import Model.productVariant;
import Service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "productServlet", urlPatterns = {"/products"})
public class productServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(productServlet.class.getName());
    private ProductService productService;
    private static final int PAGE_SIZE = 20;
    private static final String ADMIN_LIST_PAGE = "PRODUCT/ListProductForAdmin.jsp";
    private static final String USER_LIST_PAGE = "PRODUCT/ProductList.jsp";

    @Override
    public void init() throws ServletException {
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "create": showCreateForm(request, response); break;
                case "edit":   showEditForm(request, response);   break;
                case "delete": deleteProduct(request, response);  break;
                default:        listProducts(request, response);   break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error processing request", ex);
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "create": insertProduct(request, response); break;
                case "update": updateProduct(request, response); break;
                case "delete": deleteProduct(request, response); break;
                default:        listProducts(request, response); break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error processing form", ex);
            throw new ServletException(ex);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
        }
        List<product> products = productService.getProductsByPage(page, PAGE_SIZE);
        int totalPages = productService.getTotalPages(PAGE_SIZE);
        request.setAttribute("productList", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("noOfPages", totalPages);

        String role = (String) request.getSession().getAttribute("role");
        String targetPage = "user".equalsIgnoreCase(role) ? USER_LIST_PAGE : ADMIN_LIST_PAGE;
        RequestDispatcher dispatcher = request.getRequestDispatcher(targetPage);
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("PRODUCT/CreateProduct.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        product existing = productService.getProductById(id);
        request.setAttribute("product", existing);
        RequestDispatcher dispatcher = request.getRequestDispatcher("PRODUCT/EditProduct.jsp");
        dispatcher.forward(request, response);
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        product p = new product();
        p.setName(request.getParameter("name"));
        p.setDescription(request.getParameter("description"));
        p.setPrice(new BigDecimal(request.getParameter("price")));
        p.setCategory(request.getParameter("category"));
        p.setStatus("1".equals(request.getParameter("status")));

        // --- parse variants như cũ ---
        String[] sizes = request.getParameterValues("variantSize");
        String[] qtys  = request.getParameterValues("variantQuantity");
        List<productVariant> variants = new ArrayList<>();
        if (sizes != null && qtys != null) {
            for (int i = 0; i < sizes.length; i++) {
                productVariant v = new productVariant();
                v.setSize(sizes[i]);
                v.setQuantity(Integer.parseInt(qtys[i]));
                variants.add(v);
            }
        }
        p.setVariants(variants);

        // --- parse images bằng radio primaryImage ---
        String[] urls       = request.getParameterValues("imageUrl");
        String primaryIndex = request.getParameter("primaryImage");
        
        List<productImage> images = new ArrayList<>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                productImage img = new productImage();
                img.setImageUrl(urls[i]);
                // chỉ đánh isPrimary nếu index == primaryIndex
                img.setIsPrimary(String.valueOf(i).equals(primaryIndex));
                images.add(img);
            }
        }
        p.setImages(images);

        productService.addProduct(p);
        response.sendRedirect("products?action=list");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        product p = new product();
        p.setId(id);
        p.setName(request.getParameter("name"));
        p.setDescription(request.getParameter("description"));
        p.setPrice(new BigDecimal(request.getParameter("price")));
        p.setCategory(request.getParameter("category"));
        p.setStatus("1".equals(request.getParameter("status")));

        // Parse variants
        String[] sizes = request.getParameterValues("variantSize");
        String[] qtys = request.getParameterValues("variantQuantity");
        List<productVariant> variants = new ArrayList<>();
        if (sizes != null && qtys != null) {
            for (int i = 0; i < sizes.length; i++) {
                productVariant v = new productVariant();
                v.setProductId(id);
                v.setSize(sizes[i]);
                v.setQuantity(Integer.parseInt(qtys[i]));
                variants.add(v);
            }
        }
        p.setVariants(variants);

        // Parse images
        String[] urls = request.getParameterValues("imageUrl");
        String[] primaries = request.getParameterValues("isPrimary");
        List<productImage> images = new ArrayList<>();
        if (urls != null) {
            List<String> priList = primaries != null ? Arrays.asList(primaries) : new ArrayList<>();
            for (String url : urls) {
                productImage img = new productImage();
                img.setProductId(id);
                img.setImageUrl(url);
                img.setIsPrimary(priList.contains(url));
                images.add(img);
            }
        }
        p.setImages(images);

        productService.updateProduct(p);
        String page = request.getParameter("page");
        response.sendRedirect("products?action=list&page=" + (page != null ? page : "1"));
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productService.deleteProduct(id);
        String page = request.getParameter("page");
        response.sendRedirect("products?action=list&page=" + (page != null ? page : "1"));
    }
}
