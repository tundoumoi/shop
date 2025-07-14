    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
     */
    package Controller;

    import Model.User;
    import Model.product;
    import Model.productImage;
    import Model.productVariant;
    import Service.ProductService;
    import DAO.ProductViewDAO;
import DAO.RecommendationDAO;
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
     * @HP
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
                    case "view":   recordProductView(request, response); break;
                    case "viewTime": recordViewTime(request, response); break;
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
                case "viewTime": recordViewTime(request, response); break; // ✅ THÊM DÒNG NÀY
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
        try {
            page = Integer.parseInt(pageParam);
        } catch (NumberFormatException ignored) {}
    }

    String category = request.getParameter("category");
    String description = request.getParameter("description");

    List<product> products;
    int totalPages;

    if (category != null && !category.isEmpty()) {
        if (description != null && !description.isEmpty()) {
            products = productService.getProductsByCategoryAndDescription(category, description, page, PAGE_SIZE);
            totalPages = productService.getTotalPagesByCategoryAndDescription(category, description, PAGE_SIZE);
        } else {
            products = productService.getProductsByCategory(category, page, PAGE_SIZE);
            totalPages = productService.getTotalPagesByCategory(category, PAGE_SIZE);
        }
        request.setAttribute("category", category);
        request.setAttribute("description", description);
    } else {
        products = productService.getProductsByPage(page, PAGE_SIZE);
        totalPages = productService.getTotalPages(PAGE_SIZE);
    }

    request.setAttribute("productList", products);
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPages", totalPages);

    User user = (User) request.getSession().getAttribute("user");

    // ✅ Thêm đoạn này để truyền danh sách gợi ý
    if (user != null && "user".equalsIgnoreCase(user.getRole())) {
        RecommendationDAO rdao = new RecommendationDAO();
        List<product> recList = rdao.getTopRecommendedProducts(user.getId());
        request.setAttribute("recommended", recList);
    }

    String role = user.getRole();
    String targetPage = "user".equalsIgnoreCase(role) ? USER_LIST_PAGE : ADMIN_LIST_PAGE;

    RequestDispatcher dispatcher = request.getRequestDispatcher(targetPage);
    dispatcher.forward(request, response);
}



    private void recordViewTime(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            int viewTime = Integer.parseInt(request.getParameter("time"));
            User user = (User) request.getSession().getAttribute("user");

            if (user != null && viewTime > 0) {
                ProductViewDAO viewDAO = new ProductViewDAO();
                viewDAO.recordView(user.getId(), productId, viewTime);
            }

            // Không cần trả về gì
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
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
        private void recordProductView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));

            // Ghi lượt xem mặc định là 1 giây nếu muốn
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                ProductViewDAO dao = new ProductViewDAO();
                dao.recordView(user.getId(), productId, 1); // hoặc 0 nếu bạn chỉ tính bằng beacon
            }

            // Lấy dữ liệu sản phẩm
            product p = productService.getProductById(productId);
            List<productVariant> variants = productService.getVariantsByProductId(productId);

            // Gán vào request
            request.setAttribute("product", p);
            request.setAttribute("variants", variants);

            // Forward đến trang chi tiết
            request.getRequestDispatcher("PRODUCT/ProductInfo.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hiển thị sản phẩm.");
        }
    }


    }
