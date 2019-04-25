package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.RequestItems;
import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.repository.RequestItemRepository;
import com.dw.ngms.cis.im.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class RequestItemService {

    @Autowired
    private RequestItemRepository requestItemRepository;


    public List<RequestItems> getAllRequestsItems() {

        return this.requestItemRepository.findAll();
    }//getAllRequestsItems



    public Long getRequestItemId() {
        return this.requestItemRepository.getRequestItemId();
    } //getRequestTypeID


    public RequestItems saveRequestItem(RequestItems requestItems) {
        return this.requestItemRepository.save(requestItems);
    } //saveRequestItem



    public RequestItems getRequestsByRequestCodeAndItemCode(String requestCode, String requestItemCode) {
        return this.requestItemRepository.getRequestsByRequestCodeAndItemCode(requestCode,requestItemCode);
    } //getRequestsByRequestCodeAndItemCode

    public List<RequestItems>  getRequestsByRequestCode(String requestCode) {
        return this.requestItemRepository.getRequestsByRequestCode(requestCode);
    } //getRequestsByRequestCode


    public void deleteRequestItem(RequestItems requestItems) {
         this.requestItemRepository.delete(requestItems);
    } //deleteRequestItem





}
