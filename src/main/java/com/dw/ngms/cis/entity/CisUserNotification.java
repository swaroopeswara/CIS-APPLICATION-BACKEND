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
@Table(name = "CISUSERNOTIFICATION")
public class CisUserNotification implements Serializable {
	
	private static final long serialVersionUID = 3209158392625197416L;

	@Id
	@Column(name="USERID")
	private Long userId;
	
	@Id
	@Column(name="NOTIFICATIONID")
	private Long notificationId;
	
}
