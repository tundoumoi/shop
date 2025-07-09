/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.revenueDAO;
import java.math.BigDecimal;
import java.util.Map;


public class revenueService {
     private final revenueDAO dao = new revenueDAO();

    /** Trả về map tháng → doanh thu */
    public Map<String, BigDecimal> fetchMonthlyRevenue() {
        return dao.getMonthlyRevenue();
    }
    
    public Map<String, BigDecimal> fetchCategoryRevenueCurrentMonth() {
        return dao.getCurrentMonthRevenueByCategory();
    }

}
