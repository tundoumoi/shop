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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "promotions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Promotions.findAll", query = "SELECT p FROM Promotions p"),
    @NamedQuery(name = "Promotions.findById", query = "SELECT p FROM Promotions p WHERE p.id = :id"),
    @NamedQuery(name = "Promotions.findByPromoCode", query = "SELECT p FROM Promotions p WHERE p.promoCode = :promoCode"),
    @NamedQuery(name = "Promotions.findByDescription", query = "SELECT p FROM Promotions p WHERE p.description = :description"),
    @NamedQuery(name = "Promotions.findByStartDate", query = "SELECT p FROM Promotions p WHERE p.startDate = :startDate"),
    @NamedQuery(name = "Promotions.findByEndDate", query = "SELECT p FROM Promotions p WHERE p.endDate = :endDate"),
    @NamedQuery(name = "Promotions.findByDiscountType", query = "SELECT p FROM Promotions p WHERE p.discountType = :discountType"),
    @NamedQuery(name = "Promotions.findByDiscountValue", query = "SELECT p FROM Promotions p WHERE p.discountValue = :discountValue"),
    @NamedQuery(name = "Promotions.findByConditionThreshold", query = "SELECT p FROM Promotions p WHERE p.conditionThreshold = :conditionThreshold"),
    @NamedQuery(name = "Promotions.findByApplicableCategoryId", query = "SELECT p FROM Promotions p WHERE p.applicableCategoryId = :applicableCategoryId"),
    @NamedQuery(name = "Promotions.findByApplicableProductId", query = "SELECT p FROM Promotions p WHERE p.applicableProductId = :applicableProductId"),
    @NamedQuery(name = "Promotions.findByStatus", query = "SELECT p FROM Promotions p WHERE p.status = :status"),
    @NamedQuery(name = "Promotions.findByCreatedAt", query = "SELECT p FROM Promotions p WHERE p.createdAt = :createdAt")})
public class Promotions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "promo_code")
    private String promoCode;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "discount_type")
    private String discountType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Column(name = "condition_threshold")
    private BigDecimal conditionThreshold;
    @Size(max = 50)
    @Column(name = "applicable_category_id")
    private String applicableCategoryId;
    @Column(name = "applicable_product_id")
    private Integer applicableProductId;
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Promotions() {
    }

    public Promotions(Integer id) {
        this.id = id;
    }

    public Promotions(Integer id, String promoCode, Date startDate, Date endDate, String discountType, BigDecimal discountValue) {
        this.id = id;
        this.promoCode = promoCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
        this.discountValue = discountValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getConditionThreshold() {
        return conditionThreshold;
    }

    public void setConditionThreshold(BigDecimal conditionThreshold) {
        this.conditionThreshold = conditionThreshold;
    }

    public String getApplicableCategoryId() {
        return applicableCategoryId;
    }

    public void setApplicableCategoryId(String applicableCategoryId) {
        this.applicableCategoryId = applicableCategoryId;
    }

    public Integer getApplicableProductId() {
        return applicableProductId;
    }

    public void setApplicableProductId(Integer applicableProductId) {
        this.applicableProductId = applicableProductId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
        if (!(object instanceof Promotions)) {
            return false;
        }
        Promotions other = (Promotions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.Promotions[ id=" + id + " ]";
    }
    
}
