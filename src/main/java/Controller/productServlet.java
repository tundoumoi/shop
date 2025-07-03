/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.productDAO;
import Model.product;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "productServlet", urlPatterns = {"/products"})
public class productServlet extends HttpServlet {

    private static final int PAGE_SIZE = 20;
    
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getParameter("action");
//        if(action==null){
//            action="";
//        }
//        try{
//            switch(action){
//                case "create" :
//                    showCreateForm(request, response);
//                    break;
//                case "edit" :
//                    showEditForm(request, response);
//                    break;
//                case "delete" :
//                    deleteProduct(request, response);
//                    break;
//                default :
//                    listProduct(request, response);
//                    break;
//            }
//        }catch(SQLException ex){
//            throw new ServletException(ex);
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    public void listAllProductActive(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // 1. Xác định trang hiện tại
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1; // nếu parse lỗi thì về trang 1
            }
        }

        // 2. Tính offset cho phân trang
        int offset = (page - 1) * PAGE_SIZE;

        // 3. Lấy dữ liệu từ DAO
        productDAO dao = new productDAO();
        int totalCount = dao.countAllActiveProducts();  // bạn cần có method này trong DAO
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        List<product> productList = dao.selectAllActiveProducts(offset, PAGE_SIZE);

        // 4. Đưa vào request và điều hướng đến JSP
        request.setAttribute("productList", productList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages",   totalPages);
        RequestDispatcher dispatcher = request.getRequestDispatcher("PRODUCT/ProductList.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            listAllProductActive(req, resp);
        } catch (SQLException ex) {
            Logger.getLogger(productServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
