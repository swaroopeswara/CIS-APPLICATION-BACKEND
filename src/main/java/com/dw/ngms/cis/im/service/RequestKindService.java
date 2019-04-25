package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.entity.RequestKinds;
import com.dw.ngms.cis.im.entity.RequestTypes;
import com.dw.ngms.cis.im.repository.CostCategoryRepository;
import com.dw.ngms.cis.im.repository.RequestKindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class RequestKindService {

    @Autowired
    private RequestKindRepository requestKindRepository;


    public Long getRequestKind() {
        return this.requestKindRepository.getRequestKind();
    } //getRequestKind


    public RequestKinds saveRequestKind(RequestKinds requestKinds) {
        return this.requestKindRepository.save(requestKinds);
    } //saveRequestKind

    public List<RequestKinds> getAlRequestKinds() {
        return this.requestKindRepository.findAll();
    } //getAlRequestKinds




}
