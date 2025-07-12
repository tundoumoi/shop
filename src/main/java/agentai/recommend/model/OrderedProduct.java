/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class OrderedProduct {
    private int id;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String category;
    private String size;
    private int quantity;

    public OrderedProduct() {}
    public OrderedProduct(int id, String name, String description, BigDecimal unitPrice, String category, String size, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
        this.size = size;
        this.quantity = quantity;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
