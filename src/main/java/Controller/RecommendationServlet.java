package Controller;

import DAO.RecommendationDAO;
import Model.User;
import Model.product;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
    private RecommendationDAO dao = new RecommendationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            List<product> recommended = dao.getTopRecommendedProducts(user.getId());
            if (!recommended.isEmpty()) {
                req.setAttribute("recommended", recommended);
            }
        }
        req.getRequestDispatcher("PRODUCT/recommendation.jsp").forward(req, resp);
    }
}
