package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "TASKLIFECYCLE")
public class TaskLifeCycle implements Serializable {

	private static final long serialVersionUID = -1159573371875203609L;

	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TASKID")
    private Long taskId;

    @Column(name = "TASKCODE")
    private String taskCode;

    @Column(name = "TASKTYPE", length = 50, unique=true)
    private String taskType;

    @Column(name = "TASKREFERENCECODE", length = 50, unique=true)
    private String taskReferenceCode;

    @Column(name = "TASKREFERENCETYPE", length = 100, unique=true)
    private String taskReferenceType;

    @Column(name = "TASKOPENDESC", length = 1000, unique=true)
    private String taskOpenDesc;

    @Column(name = "TASKALLOCPROVINCECODE", length = 50, unique=true)
    private String taskAllProvinceCode;

    @Column(name = "TASKALLOCSECTIONCODE", length = 50, unique=true)
    private String taskAllOCSectionCode;

    @Column(name = "TASKALLOCROLECODE", length = 50, unique=true)
    private String taskAllOCRoleCode;

    @Column(name = "TASKSTATUS", length = 30, unique=true)
    private String taskStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "TASKOPENDATE")
    private Date taskOpenDate = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "TASKCLOSEDATE")
    private Date taskCloseDate;

    @Column(name = "TASKCLOSEDESC", length = 100, unique=true)
    private String taskCLoseDESC;

    @Column(name = "TASKDONEUSERCODE", length = 100, unique=true)
    private String taskDoneUserCode;

    @Column(name = "TASKDONEUSERNAME", length = 100, unique=true)
    private String taskDoneUserName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATEDDATE", nullable = true)
    private Date updatedDate = new Date();
    
}
