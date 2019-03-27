package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.repository.InternalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/26.
 */

@Service
public class InternalUserService {

    @Autowired
    InternalUserRepository internalUserRepository;



}
