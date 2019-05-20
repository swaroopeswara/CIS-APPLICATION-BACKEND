package com.dw.ngms.cis.im.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.im.entity.ApplicationProperties;
import com.dw.ngms.cis.im.repository.ApplicationPropertiesRepository;

/**
 * Created by swaroop on 2019/04/16.
 */
@Service
public class ApplicationPropertiesService {

    @Autowired
    private ApplicationPropertiesRepository applicationPropertiesRepository;

    public List<ApplicationProperties> getPropertyValueByName(String name) {
        return this.applicationPropertiesRepository.getPropertyValueByName(name);
    }//getPropertyValueByName

    public ApplicationProperties getProperty(String key) {
        return this.applicationPropertiesRepository.getProperty(key);
    }//getPropertyValueByName
    
    public ApplicationProperties saveProperties(ApplicationProperties applicationProperties) {
        return this.applicationPropertiesRepository.save(applicationProperties);
    } //saveProperties


}
