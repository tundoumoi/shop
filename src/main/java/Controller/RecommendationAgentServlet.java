/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import agentai.recommend.agents.RecommendationAgent;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "RecommendationAgentServlet", urlPatterns = {"/runrecommendationagent"})
public class RecommendationAgentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        try {
            // Gọi cơ chế tính recommendation
            RecommendationAgent.runAllUsers();
            resp.getWriter().write("Success");
        } catch (Exception e) {
            // Trả về lỗi 500 nếu có exception
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
