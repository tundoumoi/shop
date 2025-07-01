/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class product implements Serializable{
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean status;
    private LocalDateTime createdAt;

    private List<productVariant> variants;
    private List<productImage> images;

    public product() {}

    public product(int id, String name, String description, BigDecimal price,
                   String category, boolean status, LocalDateTime createdAt,
                   List<productVariant> variants, List<productImage> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.variants = variants;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<productVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<productVariant> variants) {
        this.variants = variants;
    }

    public List<productImage> getImages() {
        return images;
    }

    public void setImages(List<productImage> images) {
        this.images = images;
    }
}
