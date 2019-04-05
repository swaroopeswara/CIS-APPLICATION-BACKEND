package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.dto.ExternalUserDTO;
import com.dw.ngms.cis.uam.dto.UserDTO;
import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.ExternalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/28.
 */
@Service
public class ExternalUserService {

    @Autowired
    ExternalUserRepository externalUserRepository;


    public ExternalUser updateSecurityQuestions(ExternalUser externalUser) {

        return this.externalUserRepository.save(externalUser);
    }

    public ExternalUser findByUserCode(UserDTO userDTO) {
        return this.externalUserRepository.findByUserCode(userDTO.getUsercode());
    }

    public void deleteAssistant(ExternalUser externalUser) {
    this.externalUserRepository.delete(externalUser);
    }


    public ExternalUser updateExternalUser(ExternalUserDTO uiUser) {
        if(uiUser == null || uiUser.getUsercode() == null) return null;
        ExternalUser externalUser = externalUserRepository.findByUserCode(uiUser.getUsercode());
        System.out.println("Tst update" +externalUser.getUsercode());
        if(externalUser == null) return null;
        return this.externalUserRepository.save(getPopulatedUserWithModifiedDetails(externalUser, uiUser));
    }//updateExternalUser


    private ExternalUser getPopulatedUserWithModifiedDetails(ExternalUser user, ExternalUserDTO uiUser) {
        System.out.println("Communication code" +uiUser.getCommunicationmodetypecode());
        if(uiUser.getOrganizationtypecode() != null && uiUser.getOrganizationtypecode() != ""){
            user.setOrganizationtypecode(uiUser.getOrganizationtypecode());
        }

        if(uiUser.getOrganizationtypename() != null && uiUser.getOrganizationtypecode() != "") user.setOrganizationtypename(uiUser.getOrganizationtypename());
        if(uiUser.getSectorCode() != null && uiUser.getSectorCode() != "") user.setSectorCode(uiUser.getSectorCode());
        if(uiUser.getSectorName() != null && uiUser.getSectorName() != "") user.setSectorName(uiUser.getSectorName());
        if(uiUser.getPostaladdressline1() != null && uiUser.getPostaladdressline1() != "") user.setPostaladdressline1(uiUser.getPostaladdressline1());
        if(uiUser.getPostaladdressline2() != null && uiUser.getPostaladdressline2() != "") user.setPostaladdressline2(uiUser.getPostaladdressline2());
        if(uiUser.getPostaladdressline3() != null && uiUser.getPostaladdressline3() != "") user.setPostaladdressline3(uiUser.getPostaladdressline3());
        if(uiUser.getPostalcode() != null && uiUser.getPostalcode() != "") user.setPostalcode(uiUser.getPostalcode());
        if(uiUser.getCommunicationmodetypecode() != null && uiUser.getCommunicationmodetypecode() != "") user.setCommunicationmodetypecode(uiUser.getCommunicationmodetypecode());
        if(uiUser.getCommunicationmodetypename() != null && uiUser.getCommunicationmodetypename() != "") user.setCommunicationmodetypename(uiUser.getCommunicationmodetypename());
        if(uiUser.getSubscribenotifications() != null && uiUser.getSubscribenotifications() != "") user.setSubscribenotifications(uiUser.getSubscribenotifications());
        if(uiUser.getSubscribeevents() != null && uiUser.getSubscribeevents() != "") user.setSubscribeevents(uiUser.getSubscribeevents());
        if(uiUser.getSubscribenews() != null && uiUser.getSubscribenews() != "") user.setSubscribenews(uiUser.getSubscribenews());
        return user;
    }//getPopulatedUserWithModifiedDetails






}
