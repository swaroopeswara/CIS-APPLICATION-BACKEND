package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/24.
 */
@Entity
@Table(name = "EXTERNALUSERASSISTANTS")
@Data
@Getter
@Setter
@ToString
public class ExternalUserAssistant implements Serializable {

	private static final long serialVersionUID = 432458826856147391L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXTERNALASSISTANTID")
    private Long externalassistantid;

    @Column(name = "USERID",nullable = true)
    private Long userid;

    @Column(name = "SURVEYORUSERCODE",nullable = true, length = 50)
    private String surveyorusercode;

    @Column(name = "SURVEYORUSERNAME",nullable = true, length = 255)
    private String surveyorusername;

    @Column(name = "ASSISTANTUSERCODE",nullable = true, length = 50)
    private String assistantusercode;

    @Column(name = "ASSISTANTUSERNAME",nullable = true, length = 255)
    private String assistantusername;

    @Column(name = "REJECTIONREASON",nullable = true, length = 500)
    private String rejectionreason;

    @Column(name = "APPREJDATE")
    private Date apprejdate;

    @Column(name = "ISAPPROVED",nullable = true, length = 500)
    private String isApproved;

    @Column(name = "CREATEDDATE",nullable = true)
    private Date createddate;

	public Long getExternalassistantid() {
		return externalassistantid;
	}

	public void setExternalassistantid(Long externalassistantid) {
		this.externalassistantid = externalassistantid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getSurveyorusercode() {
		return surveyorusercode;
	}

	public void setSurveyorusercode(String surveyorusercode) {
		this.surveyorusercode = surveyorusercode;
	}

	public String getSurveyorusername() {
		return surveyorusername;
	}

	public void setSurveyorusername(String surveyorusername) {
		this.surveyorusername = surveyorusername;
	}

	public String getAssistantusercode() {
		return assistantusercode;
	}

	public void setAssistantusercode(String assistantusercode) {
		this.assistantusercode = assistantusercode;
	}

	public String getAssistantusername() {
		return assistantusername;
	}

	public void setAssistantusername(String assistantusername) {
		this.assistantusername = assistantusername;
	}

	public String getRejectionreason() {
		return rejectionreason;
	}

	public void setRejectionreason(String rejectionreason) {
		this.rejectionreason = rejectionreason;
	}

	public Date getApprejdate() {
		return apprejdate;
	}

	public void setApprejdate(Date apprejdate) {
		this.apprejdate = apprejdate;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

    
}
