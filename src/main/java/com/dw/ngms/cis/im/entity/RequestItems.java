package com.dw.ngms.cis.im.entity;

import com.dw.ngms.cis.uam.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by swaroop on 2019/04/19.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "IMREQUESTITEMS")
public class RequestItems implements Serializable {

    @Column(name = "REQUESTID", nullable = false, length = 50, insertable = false, updatable = false)
    private Long requestId;

    @Id
    @Column(name = "REQUESTITEMID")
    @SequenceGenerator(name = "generator", sequenceName = "REQUESTITEMS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "generator")
    private Long requestItemId;


    @Column(name = "REQUESTITEMCODE")
    private String requestItemCode;

    @Column(name = "RESULTID")
    private String resultId;

    @Column(name = "SEARCHTYPE")
    private String searchType;

    @Column(name = "SEARCHTEXT")
    private String searchText;


    @Column(name = "REQUESTGAZZETTETYPE", length = 200)
    private String requestGazetteType;

    @Column(name = "REQUESTGAZZETTE1")
    private String requestGazette1;

    @Column(name = "REQUESTGAZZETTE2", length = 100, unique = true)
    private String requestGazette2;

    @Column(name = "GAZETTETYPE1", length = 200)
    private String gazetteType1;

    @Column(name = "GAZETTETYPE2", length = 200)
    private String gazetteType2;


    @Column(name = "REQUESTCOST", length = 200)
    private String requestCost;

    @Column(name = "REQUESTHOURS", length = 200)
    private String requestHours;


    @Temporal(TemporalType.DATE)
    @Column(name = "CREATEDDATE", nullable = true)
    private Date createdDate = new Date();

    @Column(name = "REQUESTCODE", nullable = true, length = 50)
    private String requestCode;

    @Column(name = "RESULTJSON", length = 2000)
    private String resultJson;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="REQUESTID", nullable=false)
    @JsonBackReference
    private Requests requestItems;

}
