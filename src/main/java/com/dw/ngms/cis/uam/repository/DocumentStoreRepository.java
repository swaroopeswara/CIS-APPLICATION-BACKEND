package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.DashBoard;
import com.dw.ngms.cis.uam.entity.DocumentStore;
import com.dw.ngms.cis.uam.entity.ExternalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/12.
 */
@Repository
public interface DocumentStoreRepository extends JpaRepository<DocumentStore, Long> {



    @Query(value = "SELECT document_seq.nextval FROM dual", nativeQuery =
            true)
    Long getDocumentID();


    @Query("SELECT u FROM DocumentStore u WHERE u.documentStoreCode = :documentStoreCode")
    DocumentStore getDocumentByID(@Param("documentStoreCode") String documentStoreCode);



}
