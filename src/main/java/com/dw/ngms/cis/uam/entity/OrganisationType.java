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
@Table(name = "ORGANIZATION")
public class OrganisationType implements Serializable {

	private static final long serialVersionUID = -3791837488494889093L;

	@Column(name = "ORGANIZATIONID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; 
	
	@Column(name = "ORGCODE", nullable = false, length = 30)
	private String organizationCode;
	
	@Column(name = "ORGNAME", nullable = false, length = 10)
	private String organizatioName;
	
	@Column(name = "ADDRESS", nullable = true, length = 500)
	private String address;
	
	@Column(name = "DESCRIPTION", nullable = false, length = 500)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ISACTIVE", nullable = false, length = 10)
	private Status isActive;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATEDDATE", nullable = false, length = 10)
	private Date createdDate;

	public OrganisationType() {
	}

	public OrganisationType(Long id, String organizationCode, String organizatioName, String address,
			String description, Status isActive, Date createdDate) {
		this.id = id;
		this.organizationCode = organizationCode;
		this.organizatioName = organizatioName;
		this.address = address;
		this.description = description;
		this.isActive = isActive;
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public String getOrganizatioName() {
		return organizatioName;
	}

	public String getAddress() {
		return address;
	}

	public String getDescription() {
		return description;
	}

	public Status getIsActive() {
		return isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public void setOrganizatioName(String organizatioName) {
		this.organizatioName = organizatioName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setIsActive(Status isActive) {
		this.isActive = isActive;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
		OrganisationType other = (OrganisationType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrganisationType [id=" + id + ", organizationCode=" + organizationCode + ", organizatioName="
				+ organizatioName + ", address=" + address + ", description=" + description + ", isActive=" + isActive
				+ ", createdDate=" + createdDate + "]";
	}

}

	
	
