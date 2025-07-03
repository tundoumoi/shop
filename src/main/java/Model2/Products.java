/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model2;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "products")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p"),
    @NamedQuery(name = "Products.findById", query = "SELECT p FROM Products p WHERE p.id = :id"),
    @NamedQuery(name = "Products.findByName", query = "SELECT p FROM Products p WHERE p.name = :name"),
    @NamedQuery(name = "Products.findByDescription", query = "SELECT p FROM Products p WHERE p.description = :description"),
    @NamedQuery(name = "Products.findByPrice", query = "SELECT p FROM Products p WHERE p.price = :price"),
    @NamedQuery(name = "Products.findByCategory", query = "SELECT p FROM Products p WHERE p.category = :category"),
    @NamedQuery(name = "Products.findByStatus", query = "SELECT p FROM Products p WHERE p.status = :status"),
    @NamedQuery(name = "Products.findByCreatedAt", query = "SELECT p FROM Products p WHERE p.createdAt = :createdAt")})
public class Products implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Size(max = 50)
    @Column(name = "category")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recommendedProductId")
    private Collection<Recommendations> recommendationsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<ProductViews> productViewsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<ProductImages> productImagesCollection;
    @OneToMany(mappedBy = "productId")
    private Collection<ProductVariants> productVariantsCollection;

    public Products() {
    }

    public Products(Integer id) {
        this.id = id;
    }

    public Products(Integer id, String name, BigDecimal price, boolean status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Collection<ProductImages> getProductImagesCollection() {
        return productImagesCollection;
    }

    public void setProductImagesCollection(Collection<ProductImages> productImagesCollection) {
        this.productImagesCollection = productImagesCollection;
    }

    @XmlTransient
    public Collection<ProductVariants> getProductVariantsCollection() {
        return productVariantsCollection;
    }

    public void setProductVariantsCollection(Collection<ProductVariants> productVariantsCollection) {
        this.productVariantsCollection = productVariantsCollection;
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
        if (!(object instanceof Products)) {
            return false;
        }
        Products other = (Products) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.Products[ id=" + id + " ]";
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Recommendations> getRecommendationsCollection() {
        return recommendationsCollection;
    }

    public void setRecommendationsCollection(Collection<Recommendations> recommendationsCollection) {
        this.recommendationsCollection = recommendationsCollection;
    }

    @XmlTransient
    public Collection<ProductViews> getProductViewsCollection() {
        return productViewsCollection;
    }

    public void setProductViewsCollection(Collection<ProductViews> productViewsCollection) {
        this.productViewsCollection = productViewsCollection;
    }
    
    /**
    * Trả về trạng thái của đối tượng.
    * @return true nếu đang hoạt động; false nếu không hoạt động.
    */
   public boolean isStatus() {
       return this.status;
   }
}
