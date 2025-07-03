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
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "order_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderItems.findAll", query = "SELECT o FROM OrderItems o"),
    @NamedQuery(name = "OrderItems.findById", query = "SELECT o FROM OrderItems o WHERE o.id = :id"),
    @NamedQuery(name = "OrderItems.findByQuantity", query = "SELECT o FROM OrderItems o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "OrderItems.findByUnitPrice", query = "SELECT o FROM OrderItems o WHERE o.unitPrice = :unitPrice")})
public class OrderItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Orders orderId;
    @JoinColumn(name = "variant_id", referencedColumnName = "id")
    @ManyToOne
    private ProductVariants variantId;

    public OrderItems() {
    }

    public OrderItems(Integer id) {
        this.id = id;
    }

    public OrderItems(Integer id, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Orders getOrderId() {
        return orderId;
    }

    public void setOrderId(Orders orderId) {
        this.orderId = orderId;
    }

    public ProductVariants getVariantId() {
        return variantId;
    }

    public void setVariantId(ProductVariants variantId) {
        this.variantId = variantId;
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
        if (!(object instanceof OrderItems)) {
            return false;
        }
        OrderItems other = (OrderItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.OrderItems[ id=" + id + " ]";
    }
    
}
