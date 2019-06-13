package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.entity.DocumentStore;
import com.dw.ngms.cis.uam.repository.DashBoardRepository;
import com.dw.ngms.cis.uam.repository.DocumentStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/12.
 */
@Service
public class DocumentStoreService {

    @Autowired
    private DocumentStoreRepository documentStoreRepository;

    public Long getDocumentID() {
        return this.documentStoreRepository.getDocumentID();
    } //getDocumentID



    public DocumentStore saveDocument(DocumentStore documentStore) {
        return this.documentStoreRepository.save(documentStore);
    } //saveDocument



    public DocumentStore getDocumentByID(String documentId) {
        return this.documentStoreRepository.getDocumentByID(documentId);
    } //saveDocument



}
