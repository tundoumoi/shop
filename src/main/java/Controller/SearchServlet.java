package Controller;

import DAO.productDAO;
import Model.product;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

private static final int PAGE_SIZE = 20; 


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy từ khóa tìm kiếm
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        // Lấy số trang
        int page = 1;
        String pageParam = request.getParameter("page");
        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        int offset = (page - 1) * PAGE_SIZE;

        // Gọi DAO để lấy kết quả tìm kiếm
        productDAO dao = new productDAO();
        List<product> searchResults = dao.searchProductsByName(keyword, offset, PAGE_SIZE);
        int totalResults = dao.countSearchResults(keyword);
        int totalPages = (int) Math.ceil((double) totalResults / PAGE_SIZE);

        // Gán dữ liệu cho JSP
        request.setAttribute("searchResults", searchResults);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Hiển thị trang kết quả
        RequestDispatcher dispatcher = request.getRequestDispatcher("searchResults.jsp");
        dispatcher.forward(request, response);
    }
}
