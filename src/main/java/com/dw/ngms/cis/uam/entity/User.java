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

/**
 * Created by swaroop on 2019/03/24.
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = 6118117170894690660L;

	@Id
    @Column(name = "USERID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(name = "USERCODE", nullable = true, length = 50, unique=true)
    private String userCode;

    private ExternalUser externaluser;

    @Column(name = "USERNAME", nullable = true, length = 255)
    private String userName;

    @Column(name = "USERTYPECODE", nullable = true, length = 50)
    private String userTypeCode;

    @Column(name = "USERTYPENAME", nullable = true, length = 50)
    private String userTypeName;

    @Column(name = "PASSWORD", nullable = true, length = 60)
    private String password;

    @Column(name = "TITLE",length = 15)
    private String title;

    @Column(name = "FIRSTNAME", nullable = true, length = 60)
    private String firstName;

    @Column(name = "SURNAME", nullable = true, length = 60)
    private String surname;

    @Column(name = "MOBILENO",nullable = true, length = 20)
    private String mobileNo;

    @Column(name = "TELEPHONENO", length = 20)
    private String telephoneNo;

    @Column(name = "EMAIL", nullable = true, length = 255)
    private String email;

    @Column(name = "ISAPPROVED",length = 3)
    private String isApproved;

    @Column(name = "REJECTIONREASON",length = 500)
    private String rejectionReason;

    @Column(name = "ISAPPREJUSERCODE",length = 50)
    private String isApprejuserCode;

    @Column(name = "ISAPPREJUSERNAME",length = 255)
    private String isApprejuserName;

    @Column(name = "ISAPPREJDATE")    private Date isApprejDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private Status isActive;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate;
    
//    @ManyToMany(fetch=FetchType.LAZY)
//    @JoinTable(name = "INTERNALUSERROLES", 
//      joinColumns = { @JoinColumn(name = "USERCODE") }, 
//      inverseJoinColumns = { @JoinColumn(name = "INTERNALROLECODE") })
//    private List<InternalRole> internalRoles = new ArrayList<InternalRole>();
    
    public User() {
	}
    
	public User(Long userId, String userCode, ExternalUser externaluser, String userName, String userTypeCode,
			String userTypeName, String password, String title, String firstName, String surname, String mobileNo,
			String telephoneNo, String email, String isApproved, String rejectionReason, String isApprejuserCode,
			String isApprejuserName, Date isApprejDate, Status isActive, Date createdDate) {
		this.userId = userId;
		this.userCode = userCode;
		this.externaluser = externaluser;
		this.userName = userName;
		this.userTypeCode = userTypeCode;
		this.userTypeName = userTypeName;
		this.password = password;
		this.title = title;
		this.firstName = firstName;
		this.surname = surname;
		this.mobileNo = mobileNo;
		this.telephoneNo = telephoneNo;
		this.email = email;
		this.isApproved = isApproved;
		this.rejectionReason = rejectionReason;
		this.isApprejuserCode = isApprejuserCode;
		this.isApprejuserName = isApprejuserName;
		this.isApprejDate = isApprejDate;
		this.isActive = isActive;
		this.createdDate = createdDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public ExternalUser getExternaluser() {
		return externaluser;
	}

	public void setExternaluser(ExternalUser externaluser) {
		this.externaluser = externaluser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserTypeCode() {
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getIsApprejuserCode() {
		return isApprejuserCode;
	}

	public void setIsApprejuserCode(String isApprejuserCode) {
		this.isApprejuserCode = isApprejuserCode;
	}

	public String getIsApprejuserName() {
		return isApprejuserName;
	}

	public void setIsApprejuserName(String isApprejuserName) {
		this.isApprejuserName = isApprejuserName;
	}

	public Date getIsApprejDate() {
		return isApprejDate;
	}

	public void setIsApprejDate(Date isApprejDate) {
		this.isApprejDate = isApprejDate;
	}

	public Status getIsActive() {
		return isActive;
	}

	public void setIsActive(Status isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userCode=" + userCode + ", externaluser=" + externaluser + ", userName="
				+ userName + ", userTypeCode=" + userTypeCode + ", userTypeName=" + userTypeName + ", password="
				+ password + ", title=" + title + ", firstName=" + firstName + ", surname=" + surname + ", mobileNo="
				+ mobileNo + ", telephoneNo=" + telephoneNo + ", email=" + email + ", isApproved=" + isApproved
				+ ", rejectionReason=" + rejectionReason + ", isApprejuserCode=" + isApprejuserCode
				+ ", isApprejuserName=" + isApprejuserName + ", isApprejDate=" + isApprejDate + ", isActive=" + isActive
				+ ", createdDate=" + createdDate + "]";
	}

}
