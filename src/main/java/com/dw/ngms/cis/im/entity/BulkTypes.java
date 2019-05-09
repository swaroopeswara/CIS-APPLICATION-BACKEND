package com.dw.ngms.cis.im.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMBULKTYPES")
public class BulkTypes {

    @Id
    @Column(name = "BULKTYPEID")
    @SequenceGenerator(name = "generator", sequenceName = "BULKTYPES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long bulkTypeId;

    @OneToMany(mappedBy="subBulkItems",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BulkSubTypes> subBulkItems;

    @Column(name = "BULKTYPECODE")
    private String bulkTypeCode;

    @Column(name = "BULKTYPENAME", length = 200, unique = true)
    private String bulkTypeName;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "ISACTIVE", length = 10, unique = true)
    private String isActive;


    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFIEDDATE")
    private Date modifiedDate;


    @Column(name = "ISDELETED", length = 10, unique = true)
    private String isDeleted;

    @Temporal(TemporalType.DATE)
    @Column(name = "DELETEDDATE")
    private Date deletedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();

}
