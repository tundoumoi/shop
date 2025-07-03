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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "product_variants")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductVariants.findAll", query = "SELECT p FROM ProductVariants p"),
    @NamedQuery(name = "ProductVariants.findById", query = "SELECT p FROM ProductVariants p WHERE p.id = :id"),
    @NamedQuery(name = "ProductVariants.findBySize", query = "SELECT p FROM ProductVariants p WHERE p.size = :size"),
    @NamedQuery(name = "ProductVariants.findByQuantity", query = "SELECT p FROM ProductVariants p WHERE p.quantity = :quantity")})
public class ProductVariants implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "size")
    private String size;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @OneToMany(mappedBy = "variantId")
    private Collection<CartItems> cartItemsCollection;
    @OneToMany(mappedBy = "variantId")
    private Collection<OrderItems> orderItemsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne
    private Products productId;

    public ProductVariants() {
    }

    public ProductVariants(Integer id) {
        this.id = id;
    }

    public ProductVariants(Integer id, String size, int quantity) {
        this.id = id;
        this.size = size;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof ProductVariants)) {
            return false;
        }
        ProductVariants other = (ProductVariants) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.ProductVariants[ id=" + id + " ]";
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @XmlTransient
    public Collection<CartItems> getCartItemsCollection() {
        return cartItemsCollection;
    }

    public void setCartItemsCollection(Collection<CartItems> cartItemsCollection) {
        this.cartItemsCollection = cartItemsCollection;
    }

    @XmlTransient
    public Collection<OrderItems> getOrderItemsCollection() {
        return orderItemsCollection;
    }

    public void setOrderItemsCollection(Collection<OrderItems> orderItemsCollection) {
        this.orderItemsCollection = orderItemsCollection;
    }
    
}
