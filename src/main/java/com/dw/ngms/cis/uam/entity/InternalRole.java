package com.dw.ngms.cis.uam.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Srinas Neelapala iv on 2019/03/24.
 */

@Entity
@Table(name = "INTERNALROLES")
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternalRole implements Serializable {

	private static final long serialVersionUID = 1098484893268694655L;

	@Id
    @Column(name = "INTERNALROLEID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long internalRoleId;
    
    @Column(name = "ORGCODE", nullable = true, length = 50)
    private String organisationCode;

    @Column(name = "ORGNAME", nullable = true, length = 100)
    private String organisationName;

    @Column(name = "ROLECODE", nullable = true, length = 50)
    private String roleCode;
    
    @Column(name = "ROLENAME", nullable = true, length = 100)
    private String roleName;
    
    @Column(name = "PROVINCECODE", nullable = true, length = 50)
    private String provinceCode;

    @Column(name = "PROVINCENAME", nullable = true, length = 100)
    private String provinceName;

    @Column(name = "SECTIONCODE", nullable = true, length = 100)
    private String sectionCode;

    @Column(name = "SECTIONNAME", nullable = true, length = 50)
    private String sectionName;

    @Column(name = "DESCRIPTION", nullable = true, length = 100)
    private String description;



/*    @Column(name = "ISACTIVE", nullable = true, length = 10)
    //@NotEmpty(message = "Active status must not be empty")  //// TODO: Discuss with srinivas raju garu
    private String isactive;*/

    @Column(name = "ISACTIVE", nullable = true, length = 10)
    private String isactive;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    Date createddate;

    @Column(name = "ACCESSRIGHTSJSON", length=2000)
    private String accessRightJson;

    @Column(name = "DASHBOARDRIGHTSJSON", length=2000)
    private String dashBoardRightJson;


    @Column(name = "INTERNALROLECODE",length = 50)
    private String internalRoleCode;

    @ManyToMany(mappedBy = "internalRoleList", fetch=FetchType.LAZY)
    private List<Task> taskList = new ArrayList<Task>();   
    
}
