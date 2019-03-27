package com.dw.ngms.cis.uam.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

}
