package com.dw.ngms.cis.im.service;

import com.dw.ngms.cis.im.entity.BulkTypes;
import com.dw.ngms.cis.im.entity.CostCategories;
import com.dw.ngms.cis.im.repository.BulkTypeRepository;
import com.dw.ngms.cis.im.repository.CostCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/16.
 */

@Service
public class BulkTypeService {


    @Autowired
    private BulkTypeRepository bulkTypeRepository;

    public List<BulkTypes> getActiveBulkTypes() {
        return this.bulkTypeRepository.findActiveBulkTypes();
    }//findActiveBulkTypes

    public BulkTypes saveBulk(BulkTypes bulkTypes) {
        return this.bulkTypeRepository.save(bulkTypes);
    } //saveBulk

    public Long getBulkTypeId() {
        return this.bulkTypeRepository.getBulkTypeId();
    } //getBulkTypeId




}
