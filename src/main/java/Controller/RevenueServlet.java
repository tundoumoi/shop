/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Service.inventoryService;
import Service.revenueService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author LENOVO Ideapad 3
 */
@WebServlet(name = "RevenueServlet", urlPatterns = {"/revenues"})
public class RevenueServlet extends HttpServlet {

    private final revenueService revenueService     = new revenueService();
    private final inventoryService inventoryService = new inventoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        // 1. Doanh thu
        Map<String, ?> revenues = revenueService.fetchMonthlyRevenue();

        // 2. Tồn kho theo sản phẩm
        Map<String, Integer> inventories = inventoryService.fetchInventoryByProduct();

        
        req.setAttribute("revenues", revenues);
        req.setAttribute("inventories", inventories);
        
        req.getRequestDispatcher("REVENUE/revenue.jsp")
           .forward(req, resp);
    }
}
