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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author LENOVO Ideapad 3
 */
@Entity
@Table(name = "action_logs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActionLogs.findAll", query = "SELECT a FROM ActionLogs a"),
    @NamedQuery(name = "ActionLogs.findById", query = "SELECT a FROM ActionLogs a WHERE a.id = :id"),
    @NamedQuery(name = "ActionLogs.findByActionType", query = "SELECT a FROM ActionLogs a WHERE a.actionType = :actionType"),
    @NamedQuery(name = "ActionLogs.findByStatus", query = "SELECT a FROM ActionLogs a WHERE a.status = :status"),
    @NamedQuery(name = "ActionLogs.findByPayload", query = "SELECT a FROM ActionLogs a WHERE a.payload = :payload"),
    @NamedQuery(name = "ActionLogs.findByResponse", query = "SELECT a FROM ActionLogs a WHERE a.response = :response"),
    @NamedQuery(name = "ActionLogs.findByExecutedAt", query = "SELECT a FROM ActionLogs a WHERE a.executedAt = :executedAt")})
public class ActionLogs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "action_type")
    private String actionType;
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "payload")
    private String payload;
    @Size(max = 2147483647)
    @Column(name = "response")
    private String response;
    @Column(name = "executed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date executedAt;
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BusinessReports reportId;

    public ActionLogs() {
    }

    public ActionLogs(Integer id) {
        this.id = id;
    }

    public ActionLogs(Integer id, String actionType, String payload) {
        this.id = id;
        this.actionType = actionType;
        this.payload = payload;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Date executedAt) {
        this.executedAt = executedAt;
    }

    public BusinessReports getReportId() {
        return reportId;
    }

    public void setReportId(BusinessReports reportId) {
        this.reportId = reportId;
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
        if (!(object instanceof ActionLogs)) {
            return false;
        }
        ActionLogs other = (ActionLogs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model2.ActionLogs[ id=" + id + " ]";
    }
    
}
