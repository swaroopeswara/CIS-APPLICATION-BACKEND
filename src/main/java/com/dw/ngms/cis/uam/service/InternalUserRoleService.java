package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.repository.InternalUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/03/28.
 */

@Service
public class InternalUserRoleService {

    @Autowired
    private InternalUserRoleRepository internalUserRoleRepository;
}
