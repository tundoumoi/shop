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
@Table(name = "revenue_forecast")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RevenueForecast.findAll", query = "SELECT r FROM RevenueForecast r"),
    @NamedQuery(name = "RevenueForecast.findById", query = "SELECT r FROM RevenueForecast r WHERE r.id = :id"),
    @NamedQuery(name = "RevenueForecast.findByMonth", query = "SELECT r FROM RevenueForecast r WHERE r.month = :month"),
    @NamedQuery(name = "RevenueForecast.findByPredictedRevenue", query = "SELECT r FROM RevenueForecast r WHERE r.predictedRevenue = :predictedRevenue"),
    @NamedQuery(name = "RevenueForecast.findByMethod", query = "SELECT r FROM RevenueForecast r WHERE r.method = :method"),
    @NamedQuery(name = "RevenueForecast.findByCreatedAt", query = "SELECT r FROM RevenueForecast r WHERE r.createdAt = :createdAt")})
public class RevenueForecast implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "month")
    private String month;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "predicted_revenue")
    private BigDecimal predictedRevenue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "method")
    private String method;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public RevenueForecast() {
    }

    public RevenueForecast(Integer id) {
        this.id = id;
    }

    public RevenueForecast(Integer id, String month, BigDecimal predictedRevenue, String method) {
        this.id = id;
        this.month = month;
        this.predictedRevenue = predictedRevenue;
        this.method = method;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getPredictedRevenue() {
        return predictedRevenue;
    }

    public void setPredictedRevenue(BigDecimal predictedRevenue) {
        this.predictedRevenue = predictedRevenue;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
        if (!(object instanceof RevenueForecast)) {
            return false;
        }
        RevenueForecast other = (RevenueForecast) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.RevenueForecast[ id=" + id + " ]";
    }
    
}
