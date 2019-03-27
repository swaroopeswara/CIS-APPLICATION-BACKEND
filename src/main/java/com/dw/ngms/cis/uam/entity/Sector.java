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
@Table(name = "SECTORS")
public class Sector implements Serializable {

	private static final long serialVersionUID = 4886062577781722631L;

	@Column(name = "SECTORID")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NaturalId
	@Column(name = "SECTORCODE", nullable = true, length = 50)
    private String code; 
    
    @Column(name = "SECTORNAME", nullable = true, length = 70)
    private String name; 
    
    @Column(name = "DESCRIPTION", nullable = true, length = 500)
    private String description; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private Status isActive; 
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date creationDate;

	public Sector() {
	}

	public Sector(Long id, String code, String name, String description, Status isActive, Date creationDate) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
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
		Sector other = (Sector) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sector [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + ", isActive="
				+ isActive + ", creationDate=" + creationDate + "]";
	}
    
}
