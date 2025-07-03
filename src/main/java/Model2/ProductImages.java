/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model2;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "product_images")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductImages.findAll", query = "SELECT p FROM ProductImages p"),
    @NamedQuery(name = "ProductImages.findById", query = "SELECT p FROM ProductImages p WHERE p.id = :id"),
    @NamedQuery(name = "ProductImages.findByImageUrl", query = "SELECT p FROM ProductImages p WHERE p.imageUrl = :imageUrl"),
    @NamedQuery(name = "ProductImages.findByIsPrimary", query = "SELECT p FROM ProductImages p WHERE p.isPrimary = :isPrimary")})
public class ProductImages implements Serializable {

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "is_primary")
    private Boolean isPrimary;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Products productId;

    public ProductImages() {
    }

    public ProductImages(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Products getProductId() {
        return productId;
    }

    public void setProductId(Products productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductImages)) {
            return false;
        }
        ProductImages other = (ProductImages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.ProductImages[ id=" + id + " ]";
    }
    
}
