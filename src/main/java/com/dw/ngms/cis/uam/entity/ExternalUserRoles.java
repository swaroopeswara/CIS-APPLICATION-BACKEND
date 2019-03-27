package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/24.
 */

@Entity
@Table(name = "ExternalUserRoles")
@Data
@Getter
@Setter
@ToString
public class ExternalUserRoles implements Serializable {

	private static final long serialVersionUID = 1098484893268694655L;

	@Id
    @Column(name = "USERROLEID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userRoleId;

    @Column(name = "USERROLECODE", nullable = true, length = 50)
    @NotEmpty(message = "USER ROLE CODE must not be empty")
    private String userRoleCode;

    @Column(name = "USERROLENAME", nullable = true, length = 50)
    @NotEmpty(message = "USER ROLE NAME must not be empty")
    private String userRoleName;

    @Column(name = "USERCODE", nullable = true, length = 50)
    @NotEmpty(message = "USER CODE must not be empty")
    private String userCode;
    
    @Column(name = "USERNAME", nullable = true, length = 255)
    @NotEmpty(message = "USER NAMEmust not be empty")
    private String userName;
    
    @Column(name = "USERPROVINCECODE", nullable = true, length = 100)
    @NotEmpty(message = "USER PROVINCE CODE must not be empty")
    private String userProvinceCode;

    @Column(name = "USERPROVINCENAME", nullable = true, length = 50)
    @NotEmpty(message = "USER PROVINCE NAME must not be empty")
    private String userProvinceName;

    @Column(name = "ISACTIVE", nullable = true, length = 10)
    @NotEmpty(message = "Active status must not be empty")
    private String isActive;

    @Column(name = "CREATEDDATE", nullable = true)
    Date createdDate;

//    @Column(name = "SIGNEDACCESSFILEPATH")
//    private String signedaccessfilepath;

//    @Column(name = "USERID", nullable = true)
//    private Long userid;

    @Column(name = "EXTERNALROLECODE",length = 50)
    private String externalRoleCode;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userRoleId == null) ? 0 : userRoleId.hashCode());
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
		ExternalUserRoles other = (ExternalUserRoles) obj;
		if (userRoleId == null) {
			if (other.userRoleId != null)
				return false;
		} else if (!userRoleId.equals(other.userRoleId))
			return false;
		return true;
	}
    
}
