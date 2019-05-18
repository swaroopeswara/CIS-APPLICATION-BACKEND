package com.dw.ngms.cis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CISCONFIG")
public class CisConfig implements Serializable {
	
	private static final long serialVersionUID = 4305001328371603564L;

	@Id
	private Integer id;
	
	@Column(name="NAME", length = 60)
	private String name;
	
	@Column(name="VALUE")
	private Integer value;
	
	@Column(name="TYPE", length = 20)
	private String type;
	
	@Column(name="EDITABLE", columnDefinition="boolean")
	private String isModifiable;
	
	@Column(name="DESCRIPTION", length = 120)
	private String description;
	
}
