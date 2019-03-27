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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/24.
 */

@Entity
@Getter
@Setter
@ToString
@Table(name = "EXTERNALUSERDATA")
public class ExternalUser implements Serializable {

    private static final long serialVersionUID = -955002950121265546L;

    @Id
    @Column(name = "EXTERNALUSERDATAID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long externaluserdataid;


    @Column(name = "USERCODE", nullable = true, length = 50)
    @NotEmpty(message = "USERCODE must not be empty")
    private String usercode;

    @Column(name = "ORGANIZATIONTYPECODE", nullable = true, length = 50)
    @NotEmpty(message = "ORGANIZATION TYPE CODE must not be empty")
    private String organizationtypecode;

    @Column(name = "ORGANIZATIONTYPENAME", nullable = true, length = 70)
    @NotEmpty(message = "ORGANIZATION TYPE ENAME must not be empty")
    private String organizationtypename;

    @Column(name = "PPNNO", nullable = true, length = 70)
    @NotEmpty(message = "PPNNO must not be empty")
    private String ppno;

    @Column(name = "PRACTISENAME", nullable = true, length = 100)
    @NotEmpty(message = "PRACTICE NAME must not be empty")
    private String practicename;

    @Column(name = "POSTALADDRESSLINE1", nullable = true, length = 70)
    @NotEmpty(message = "POSTAL ADDRESSLINE must not be empty")
    private String postaladdressline1;

    @Column(name = "POSTALADDRESSLINE2", nullable = true, length = 70)
    @NotEmpty(message = "POSTAL ADDRESSLINE2 must not be empty")
    private String postaladdressline2;

    @Column(name = "POSTALADDRESSLINE3", nullable = true, length = 70)
    @NotEmpty(message = "POSTAL ADDRESSLINE3 must not be empty")
    private String postaladdressline3;

    @Column(name = "POSTALCODE", nullable = true, length = 15)
    @NotEmpty(message = "POSTALCODE must not be empty")
    private String postalcode;

    @Column(name = "COMMUNICATIONMODETYPECODE", nullable = true, length = 50)
    @NotEmpty(message = "COMMUNICATION MODE TYPE CODE must not be empty")
    private String communicationmodetypecode;

    @Column(name = "COMMUNICATIONMODETYPENAME", nullable = true, length = 15)
    @NotEmpty(message = "COMMUNICATION MODE TYPE NAME must not be empty")
    private String communicationmodetypename;

    @Column(name = "SECURITYQUESTIONTYPECODE1", nullable = true, length = 50)
    @NotEmpty(message = "SECURITY QUESTIONTYPE CODE1 must not be empty")
    private String securityquestiontypecode1;

    @Column(name = "SECURITYQUESTION1", nullable = true, length = 150)
    @NotEmpty(message = "SECURITYQUESTION must not be empty")
    private String securityquestion1;

    @Column(name = "SECURITYANSWER1", nullable = true, length = 50)
    @NotEmpty(message = "SECURITYANSWER must not be empty")
    private String securityanswer1;

    @Column(name = "SECURITYQUESTIONTYPECODE2", nullable = true, length = 50)
    @NotEmpty(message = "SECURITY QUESTION TYPE CODE2 must not be empty")
    private String securityquestiontypecode2;

    @Column(name = "SECURITYQUESTION2", nullable = true, length = 150)
    @NotEmpty(message = "SECURITYQUESTION 2 must not be empty")
    private String securityquestion2;

    @Column(name = "SECURITYANSWER2", nullable = true, length = 50)
    @NotEmpty(message = "SECURITYANSWER2  must not be empty")
    private String securityanswer2;

    @Column(name = "SECURITYQUESTIONTYPECODE3", nullable = true, length = 50)
    @NotEmpty(message = "SECURITY QUESTION TYPE CODE3 must not be empty")
    private String securityquestiontypecode3;

    @Column(name = "SECURITYQUESTION3", nullable = true, length = 150)
    @NotEmpty(message = "SECURITY QUESTION3 must not be empty")
    private String securityquestion3;

    @Column(name = "SECURITYANSWER3", nullable = true, length = 50)
    @NotEmpty(message = "SECURITY ANSWER3 must not be empty")
    private String securityanswer3;

    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate;

    @Column(name = "SUBSCRIBENEWS",length = 1)
    private String subscribenews;

    @Column(name = "SUBSCRIBENOTIFICATIONS",length = 1)
    private String subscribenotifications;

    @Column(name = "SUBSCRIBEEVENTS",length = 1)
    private String subscribeevents;

    @Column(name = "USERID", nullable = true)
    private Long userid;

    @Column(name = "SECTORCODE",length = 50)
    private String sectorcode;

    @Column(name = "SECTORNAME",length = 100)
    private String sectorname;

    
	public Long getExternaluserdataid() {
		return externaluserdataid;
	}

	public void setExternaluserdataid(Long externaluserdataid) {
		this.externaluserdataid = externaluserdataid;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getOrganizationtypecode() {
		return organizationtypecode;
	}

	public void setOrganizationtypecode(String organizationtypecode) {
		this.organizationtypecode = organizationtypecode;
	}

	public String getOrganizationtypename() {
		return organizationtypename;
	}

	public void setOrganizationtypename(String organizationtypename) {
		this.organizationtypename = organizationtypename;
	}

	public String getPpno() {
		return ppno;
	}

	public void setPpno(String ppno) {
		this.ppno = ppno;
	}

	public String getPracticename() {
		return practicename;
	}

	public void setPracticename(String practicename) {
		this.practicename = practicename;
	}

	public String getPostaladdressline1() {
		return postaladdressline1;
	}

	public void setPostaladdressline1(String postaladdressline1) {
		this.postaladdressline1 = postaladdressline1;
	}

	public String getPostaladdressline2() {
		return postaladdressline2;
	}

	public void setPostaladdressline2(String postaladdressline2) {
		this.postaladdressline2 = postaladdressline2;
	}

	public String getPostaladdressline3() {
		return postaladdressline3;
	}

	public void setPostaladdressline3(String postaladdressline3) {
		this.postaladdressline3 = postaladdressline3;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getCommunicationmodetypecode() {
		return communicationmodetypecode;
	}

	public void setCommunicationmodetypecode(String communicationmodetypecode) {
		this.communicationmodetypecode = communicationmodetypecode;
	}

	public String getCommunicationmodetypename() {
		return communicationmodetypename;
	}

	public void setCommunicationmodetypename(String communicationmodetypename) {
		this.communicationmodetypename = communicationmodetypename;
	}

	public String getSecurityquestiontypecode1() {
		return securityquestiontypecode1;
	}

	public void setSecurityquestiontypecode1(String securityquestiontypecode1) {
		this.securityquestiontypecode1 = securityquestiontypecode1;
	}

	public String getSecurityquestion1() {
		return securityquestion1;
	}

	public void setSecurityquestion1(String securityquestion1) {
		this.securityquestion1 = securityquestion1;
	}

	public String getSecurityanswer1() {
		return securityanswer1;
	}

	public void setSecurityanswer1(String securityanswer1) {
		this.securityanswer1 = securityanswer1;
	}

	public String getSecurityquestiontypecode2() {
		return securityquestiontypecode2;
	}

	public void setSecurityquestiontypecode2(String securityquestiontypecode2) {
		this.securityquestiontypecode2 = securityquestiontypecode2;
	}

	public String getSecurityquestion2() {
		return securityquestion2;
	}

	public void setSecurityquestion2(String securityquestion2) {
		this.securityquestion2 = securityquestion2;
	}

	public String getSecurityanswer2() {
		return securityanswer2;
	}

	public void setSecurityanswer2(String securityanswer2) {
		this.securityanswer2 = securityanswer2;
	}

	public String getSecurityquestiontypecode3() {
		return securityquestiontypecode3;
	}

	public void setSecurityquestiontypecode3(String securityquestiontypecode3) {
		this.securityquestiontypecode3 = securityquestiontypecode3;
	}

	public String getSecurityquestion3() {
		return securityquestion3;
	}

	public void setSecurityquestion3(String securityquestion3) {
		this.securityquestion3 = securityquestion3;
	}

	public String getSecurityanswer3() {
		return securityanswer3;
	}

	public void setSecurityanswer3(String securityanswer3) {
		this.securityanswer3 = securityanswer3;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSubscribenews() {
		return subscribenews;
	}

	public void setSubscribenews(String subscribenews) {
		this.subscribenews = subscribenews;
	}

	public String getSubscribenotifications() {
		return subscribenotifications;
	}

	public void setSubscribenotifications(String subscribenotifications) {
		this.subscribenotifications = subscribenotifications;
	}

	public String getSubscribeevents() {
		return subscribeevents;
	}

	public void setSubscribeevents(String subscribeevents) {
		this.subscribeevents = subscribeevents;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getSectorcode() {
		return sectorcode;
	}

	public void setSectorcode(String sectorcode) {
		this.sectorcode = sectorcode;
	}

	public String getSectorname() {
		return sectorname;
	}

	public void setSectorname(String sectorname) {
		this.sectorname = sectorname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externaluserdataid == null) ? 0 : externaluserdataid.hashCode());
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
		ExternalUser other = (ExternalUser) obj;
		if (externaluserdataid == null) {
			if (other.externaluserdataid != null)
				return false;
		} else if (!externaluserdataid.equals(other.externaluserdataid))
			return false;
		return true;
	}

}
