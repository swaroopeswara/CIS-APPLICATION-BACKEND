package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.IssueLog;
import com.dw.ngms.cis.uam.repository.IssueLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by swaroop on 2019/04/11.
 */

@Service
public class IssueLogService {

    @Autowired
   private  IssueLogRepository issueLogRepository;

    public IssueLog saveIssueLog(IssueLog issueLog) {
        return this.issueLogRepository.save(issueLog);
    }



}
