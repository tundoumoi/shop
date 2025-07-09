/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.inventoryDAO;
import java.util.Map;

public class inventoryService {
    private final inventoryDAO dao = new inventoryDAO();

    /** Trả về map sản phẩm → số lượng tồn kho */
    public Map<String, Integer> fetchInventoryByProduct() {
        return dao.getFilteredInventory();
    }
}
