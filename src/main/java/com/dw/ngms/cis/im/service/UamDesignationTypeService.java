package com.dw.ngms.cis.im.service;


import com.dw.ngms.cis.im.entity.UamDesignations;
import com.dw.ngms.cis.im.repository.UamDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/19.
 */
@Service
public class UamDesignationTypeService {

    @Autowired
    private UamDesignationRepository uamDesignationRepository;


    public Long getUamDesignation() {
        return this.uamDesignationRepository.getUamDesignation();
    } //getUamDesignation


    public UamDesignations saveUamDesignation(UamDesignations formatTypes) {
        return this.uamDesignationRepository.save(formatTypes);
    } //saveUamDesignation


    public List<UamDesignations> getAllUamDesignations() {
        return this.uamDesignationRepository.findAll();
    } //getAllUamDesignations



    public UamDesignations findByUamDesignationCode(String designationCode) {
        return this.uamDesignationRepository.findByUamDesignationCode(designationCode);
    } //saveUamDesignation


    public void deleteUamDesignation(UamDesignations uamDesignations) {
        this.uamDesignationRepository.delete(uamDesignations);
    } //deleteUamDesignation


}
