package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.repository.RequestRepository;
import com.dw.ngms.cis.im.repository.RequestTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;


    public List<Requests> getAllRequests() {
        return this.requestRepository.findAll();
    }//getAllCostCategories

    public List<Requests> getRequestByUserCodeProvinceCode(String userCode, String provinceCode) {
        return this.requestRepository.getRequestByUserCodeProvinceCode(userCode,provinceCode);
    }//getRequestByUserCodeProvinceCode


    public Long getRequestId() {
        return this.requestRepository.getRequestId();
    } //getRequestTypeID




    public Requests saveRequest(Requests requests) {
        return this.requestRepository.save(requests);
    } //saveRequest

    public Requests getRequestsByRequestCode(String requestCode) {
        return this.requestRepository.getRequestsByRequestCode(requestCode);
    } //saveRequest





}
