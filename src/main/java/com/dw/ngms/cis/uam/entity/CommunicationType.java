package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dw.ngms.cis.uam.enums.Status;

@Entity
@Table(name = "COMMUNICATIONMODETYPES")
public class CommunicationType implements Serializable {

	private static final long serialVersionUID = -7263662741431187675L;

	@Column(name = "COMMUNICATIONMODETYPEID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "COMMUNICATIONMODETYPECODE", nullable = false, length = 50)
    private String communicationTypeCode;

    @Column(name = "COMMUNICATIONMODETYPENAME", nullable = false, length = 70)
    private String communicationTypeName;    
        
    @Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = false, length = 10)
    private Status isActive; 
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = false)
    private Date creationDate; 
       
    protected CommunicationType() {
    }
    
	public CommunicationType(Long id, String communicationTypeCode, String communicationTypeName, String description,
			Status isActive, Date creationDate) {
		super();
		this.id = id;
		this.communicationTypeCode = communicationTypeCode;
		this.communicationTypeName = communicationTypeName;
		this.description = description;
		this.isActive = isActive;
		this.creationDate = creationDate;
	}

	public Long getId() {
		return id;
	}

	public String getCommunicationTypeCode() {
		return communicationTypeCode;
	}

	public String getCommunicationTypeName() {
		return communicationTypeName;
	}

	public String getDescription() {
		return description;
	}

	public Status getIsActive() {
		return isActive;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCommunicationTypeCode(String communicationTypeCode) {
		this.communicationTypeCode = communicationTypeCode;
	}

	public void setCommunicationTypeName(String communicationTypeName) {
		this.communicationTypeName = communicationTypeName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIsActive(Status isActive) {
		this.isActive = isActive;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommunicationType other = (CommunicationType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CommunicationType [id=" + id + ", communicationTypeCode=" + communicationTypeCode
				+ ", communicationTypeName=" + communicationTypeName + ", description=" + description + ", isActive="
				+ isActive + ", creationDate=" + creationDate + "]";
	}
    
}