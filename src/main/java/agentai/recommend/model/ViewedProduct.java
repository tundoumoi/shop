/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.model;

import java.math.BigDecimal;

public class ViewedProduct {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String size;
    private BigDecimal viewTime;

    public ViewedProduct() {}
    public ViewedProduct(int id, String name, String description, BigDecimal price, String category, String size, BigDecimal viewTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.size = size;
        this.viewTime = viewTime;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public BigDecimal getViewTime() { return viewTime; }
    public void setViewTime(BigDecimal viewTime) { this.viewTime = viewTime; }
}

