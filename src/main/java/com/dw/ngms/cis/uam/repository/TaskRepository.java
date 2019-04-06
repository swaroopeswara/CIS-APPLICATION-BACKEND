package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("SELECT u FROM Task u WHERE u.taskStatus = :taskStatus and u.taskType = :taskType and u.taskAllProvinceCode = :taskAllProvinceCode and u.taskAllOCSectionCode = :taskAllOCSectionCode and u.taskAllOCRoleCode = :taskAllOCRoleCode")
    List<Task> getAllTasks(@Param("taskStatus") String taskStatus,
                              @Param("taskType") String taskType,
                              @Param("taskAllProvinceCode") String taskAllProvinceCode,
                              @Param("taskAllOCSectionCode") String taskAllOCSectionCode,
                              @Param("taskAllOCRoleCode") String taskAllOCRoleCode);



    @Query(value = "SELECT task_seq.nextval FROM dual", nativeQuery =
            true)
    Long getTaskID();


}