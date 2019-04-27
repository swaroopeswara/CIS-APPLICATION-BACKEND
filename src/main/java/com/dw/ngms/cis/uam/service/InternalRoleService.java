package com.dw.ngms.cis.uam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.repository.InternalRoleRepository;

/**
 * Created by swaroop on 2019/04/02.
 */

@Service
public class InternalRoleService {

    @Autowired
    InternalRoleRepository internalRoleRepository;


    public List<InternalRole> updateAccessRight(String roleCode) {
        return this.internalRoleRepository.updateAccessRight(roleCode);
    }

    public List<InternalRole> findByRoleCode(String roleCode) {
        return this.internalRoleRepository.findByRoleCode(roleCode);
    }


    public List<InternalRole>  updateDashBoardAccessRight(String roleCode) {
        return this.internalRoleRepository.updateDashBoardAccessRight(roleCode);
    }


    public void deleteByInternalRoleCode(InternalRole internalRole){
        this.internalRoleRepository.delete(internalRole);

    }

    public InternalRole findByInternalRoleCode(String internalRoleCode) {
        return this.internalRoleRepository.findByInternalRoleCode(internalRoleCode);
    }

    public InternalRole updateInternalRole(InternalRole internalRole) {
        return this.internalRoleRepository.save(internalRole);
    }

    public String getAccessRightJson(String internalRoleCode) {
        return this.internalRoleRepository.getAccessRightJson(internalRoleCode);
    }

    public String getdashBoardRightJson(String roleCode) {
        return this.internalRoleRepository.getdashBoardRightJson(roleCode);
    }

    public List<InternalRole> getSectionsByProvinceCode(String provinceCode) {
        return this.internalRoleRepository.getSectionsByProvinceCode(provinceCode);
    }

    public List<InternalRole> getNationalRoles() {
        return this.internalRoleRepository.getNationalRoles();
    }

    public List<InternalRole> getRolesBySectionsAndProvince(String sectionCode,String provinceCode) {
        System.out.println("Section Code is "+sectionCode);
        System.out.println("provinceCode Code is "+provinceCode);
        return this.internalRoleRepository.getRolesBySectionsAndProvince(sectionCode,provinceCode);
    }

    public List<InternalRole> getRolesBySectionsAndProvinceBySectionCodeNull(String provinceCode) {
        return this.internalRoleRepository.getRolesBySectionsAndProvinceBySectionCodeNull(provinceCode);
    }

    public List<InternalRole> getRolesBySectionsAndProvinceByProvinceCodeNull(String sectionCode) {
        return this.internalRoleRepository.getRolesBySectionsAndProvinceByProvinceCodeNull(sectionCode);
    }
    public List<InternalRole> findByProvinceCodeAndSectionCodeAndRoleName(String provinceCode, String sectionCode, String roleName) {
    	List<InternalRole> roles = new ArrayList<InternalRole>();
    	if(sectionCode != null)
    		roles = internalRoleRepository.findByProvinceCodeAndSectionCodeAndRoleName(provinceCode, sectionCode, roleName);
    	else if(provinceCode != null)
    		roles = internalRoleRepository.findByProvinceCodeAndRoleName(provinceCode, roleName);
    	else
    		roles = internalRoleRepository.findByRoleName(roleName);
//    	List<InternalRole> roles = new ArrayList<InternalRole>();
//    	roles.add(internalRoleRepository.findById(46L).get());
    	return roles;
    }//findByProvinceCodeAndSectionCodeAndRoleName
}
