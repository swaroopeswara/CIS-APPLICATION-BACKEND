package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.BulkSubTypes;
import com.dw.ngms.cis.im.entity.CostSubCategories;
import com.dw.ngms.cis.im.repository.BulkSubRepository;
import com.dw.ngms.cis.im.repository.CostSubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */
@Service
public class BulkSubService {


    @Autowired
    private BulkSubRepository bulkSubRepository;

    public Long getSubBulkID() {
        return this.bulkSubRepository.getSubBulkID();
    } //getSubBulkID

    public BulkSubTypes saveSubBulk(BulkSubTypes bulkSubTypes) {
        return this.bulkSubRepository.save(bulkSubTypes);
    } //saveSubBulk


    public List<BulkSubTypes> getBulkRequestSubTypesByTypeCode(String bulkTypeCode) {
        return this.bulkSubRepository.getBulkRequestSubTypesByTypeCode(bulkTypeCode);


    }

}
