package com.dw.ngms.cis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CISNOTIFICATION")
public class CisNotification implements Serializable {
	
	private static final long serialVersionUID = -6207972210204835351L;

	@Id
	private Integer id;
	
	@Column(name="PAYLOAD", length = 2000)
	private String payload;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATIONDATETIME")
	private Date creationDatetime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATEDATETIME")
	private Date updateDatetime;
	
	@Column(name="ISREAD", columnDefinition="boolean")
	private String isRead;
	
}
