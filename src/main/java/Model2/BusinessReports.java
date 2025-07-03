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
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "business_reports")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BusinessReports.findAll", query = "SELECT b FROM BusinessReports b"),
    @NamedQuery(name = "BusinessReports.findById", query = "SELECT b FROM BusinessReports b WHERE b.id = :id"),
    @NamedQuery(name = "BusinessReports.findByReportDate", query = "SELECT b FROM BusinessReports b WHERE b.reportDate = :reportDate"),
    @NamedQuery(name = "BusinessReports.findByReportType", query = "SELECT b FROM BusinessReports b WHERE b.reportType = :reportType"),
    @NamedQuery(name = "BusinessReports.findByContent", query = "SELECT b FROM BusinessReports b WHERE b.content = :content"),
    @NamedQuery(name = "BusinessReports.findByGeneratedBy", query = "SELECT b FROM BusinessReports b WHERE b.generatedBy = :generatedBy"),
    @NamedQuery(name = "BusinessReports.findByStatus", query = "SELECT b FROM BusinessReports b WHERE b.status = :status"),
    @NamedQuery(name = "BusinessReports.findByCreatedAt", query = "SELECT b FROM BusinessReports b WHERE b.createdAt = :createdAt")})
public class BusinessReports implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "report_date")
    @Temporal(TemporalType.DATE)
    private Date reportDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "report_type")
    private String reportType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "content")
    private String content;
    @Size(max = 50)
    @Column(name = "generated_by")
    private String generatedBy;
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportId")
    private Collection<ActionLogs> actionLogsCollection;

    public BusinessReports() {
    }

    public BusinessReports(Integer id) {
        this.id = id;
    }

    public BusinessReports(Integer id, Date reportDate, String reportType, String content) {
        this.id = id;
        this.reportDate = reportDate;
        this.reportType = reportType;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
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

    @XmlTransient
    public Collection<ActionLogs> getActionLogsCollection() {
        return actionLogsCollection;
    }

    public void setActionLogsCollection(Collection<ActionLogs> actionLogsCollection) {
        this.actionLogsCollection = actionLogsCollection;
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
        if (!(object instanceof BusinessReports)) {
            return false;
        }
        BusinessReports other = (BusinessReports) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.BusinessReports[ id=" + id + " ]";
    }
    
}
