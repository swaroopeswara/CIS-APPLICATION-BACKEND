package com.dw.ngms.cis.uam.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "NOTIFICATIONUSERTYPES")
public class NotificationTypes implements Serializable {

	@Id
	@Column(name = "NOTIFICATIONTYPECODE", length = 50)
	private String notificationTypeCode;

	@Column(name = "NOTIFICATIONTYPENAME", length = 50)
	private String notificationTypeName;

	@Column(name = "DESCRIPTION", length = 100)
	private String description;


}
