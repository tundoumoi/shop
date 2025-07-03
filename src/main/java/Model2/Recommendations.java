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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "recommendations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recommendations.findAll", query = "SELECT r FROM Recommendations r"),
    @NamedQuery(name = "Recommendations.findById", query = "SELECT r FROM Recommendations r WHERE r.id = :id"),
    @NamedQuery(name = "Recommendations.findByScore", query = "SELECT r FROM Recommendations r WHERE r.score = :score"),
    @NamedQuery(name = "Recommendations.findByAlgorithm", query = "SELECT r FROM Recommendations r WHERE r.algorithm = :algorithm"),
    @NamedQuery(name = "Recommendations.findByCreatedAt", query = "SELECT r FROM Recommendations r WHERE r.createdAt = :createdAt")})
public class Recommendations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private Double score;
    @Size(max = 50)
    @Column(name = "algorithm")
    private String algorithm;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "recommended_product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Products recommendedProductId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public Recommendations() {
    }

    public Recommendations(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Products getRecommendedProductId() {
        return recommendedProductId;
    }

    public void setRecommendedProductId(Products recommendedProductId) {
        this.recommendedProductId = recommendedProductId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
        if (!(object instanceof Recommendations)) {
            return false;
        }
        Recommendations other = (Recommendations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.Recommendations[ id=" + id + " ]";
    }
    
}
