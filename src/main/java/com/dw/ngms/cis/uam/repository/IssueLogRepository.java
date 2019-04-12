package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.InternalUserRoles;
import com.dw.ngms.cis.uam.entity.IssueLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by swaroop on 2019/04/11.
 */

@Repository
public interface IssueLogRepository extends JpaRepository<IssueLog, Long> {
}
