package com.dw.ngms.cis.uam.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "NOTIFICATIONS")
public class Notifications implements Serializable {

	private static final long serialVersionUID = -5302767605769257492L;



	@Id
	@Column(name = "NOTIFICATIONID")
	@SequenceGenerator(name = "generator", sequenceName = "NOTIFICATIONS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
	private Long notificationId;


	@Column(name = "NOTIFICATIONTYPE", length = 50)
	private String notificationType;

	@Column(name = "SUBJECT", length = 50)
	private String subject;

	@Column(name = "BODY", length = 50)
	private String body;

	@Column(name = "REGARDS", length = 50)
	private String regards;

	@Column(name = "NOTIFICATIONUSERTYPES", length = 300)
	private String notificationuserTypes;

	@Column(name = "NOTIFICATIONSTATUS", length = 50)
	private String notificationStatus;

	@Column(name = "CREATEDBYUSERNAME")
	private String createdByUserName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDDATE")
	private Date createdDate = new Date();



	
}