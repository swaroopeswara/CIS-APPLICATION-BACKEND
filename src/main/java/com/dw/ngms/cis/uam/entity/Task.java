package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by swaroop on 2019/04/06.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TASKS")
public class Task implements Serializable {

	private static final long serialVersionUID = -176045520796625793L;

	@Id
    @Column(name = "TASKID")
    @SequenceGenerator(name = "generator", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long taskId;


    @Column(name = "TASKCODE")
    @SequenceGenerator(name = "generator", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
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

    @Column(name = "TASKSTATUS", length = 10, unique=true)
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

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "ROLE_TASK",
    	joinColumns = @JoinColumn(name = "TASKID"),
        inverseJoinColumns = @JoinColumn(name = "INTERNALROLEID"))
    private List<InternalRole> internalRoleList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_TASK",
    	joinColumns = @JoinColumn(name = "TASKID"),
        inverseJoinColumns = @JoinColumn(name = "USERID"))
    private List<User> userList = new ArrayList<>();
}
