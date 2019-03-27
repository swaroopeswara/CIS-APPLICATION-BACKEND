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
@Table(name = "SECURITYQUESTIONTYPES")
@Data
@Getter
@Setter
@ToString
public class SecurityQuestion implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECURITYQUESTIONTYPEID")
    private Long securityquestiontypeid;


    @Column(name = "securityquestioncode",nullable = true, length = 50)
    private String securityquestionCode;

    @Column(name = "SECURITYQUESTION",nullable = true, length = 200)
    private String securityquestion;

    @Column(name = "ISACTIVE",nullable = true, length = 10)
    private String isactive;

    @Column(name = "CREATEDDATE",nullable = true)
    private Date createddate;



}
