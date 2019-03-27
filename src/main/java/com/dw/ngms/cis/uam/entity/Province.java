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

import org.hibernate.annotations.NaturalId;

import com.dw.ngms.cis.uam.enums.Status;

@Entity
@Table(name = "PROVINCES")
public class Province implements Serializable {

	private static final long serialVersionUID = -9126471965162438729L;

	@Column(name = "PROVINCEID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ORGCODE", nullable = true, length = 50)
    private String organisationCode;

    @Column(name = "ORGNAME", nullable = true, length = 100)
    private String organisationName;    
    
    @NaturalId
    @Column(name = "PROVINCECODE", nullable = true, length = 50)
    private String code; 
    
    @Column(name = "PROVINCENAME", nullable = true, length = 100)
    private String name; 
    
    @Column(name = "PROVINCESHORTNAME", nullable = true, length = 50)
    private String shortName; 
    
    @Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private Status isActive; 
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date creationDate; 
       
    protected Province() {
    }

	public Province(Long id, String organisationCode, String organisationName, String code, String name,
			String shortName, String description, Status isActive, Date creationDate) {
		super();
		this.id = id;
		this.organisationCode = organisationCode;
		this.organisationName = organisationName;
		this.code = code;
		this.name = name;
		this.shortName = shortName;
		this.description = description;
		this.isActive = isActive;
		this.creationDate = creationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganisationCode() {
		return organisationCode;
	}

	public void setOrganisationCode(String organisationCode) {
		this.organisationCode = organisationCode;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getIsActive() {
		return isActive;
	}

	public void setIsActive(Status isActive) {
		this.isActive = isActive;
	}

	public Date getCreationDate() {
		return creationDate;
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
		Province other = (Province) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Province [id=" + id + ", organisationCode=" + organisationCode + ", organisationName="
				+ organisationName + ", code=" + code + ", name=" + name + ", shortName=" + shortName + ", description="
				+ description + ", isActive=" + isActive + ", creationDate=" + creationDate + "]";
	}
    
}