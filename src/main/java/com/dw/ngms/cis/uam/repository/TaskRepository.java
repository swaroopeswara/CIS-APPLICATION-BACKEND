package com.dw.ngms.cis.uam.repository;

import com.dw.ngms.cis.uam.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,JpaSpecificationExecutor<Task> {


    @Query("SELECT u FROM Task u WHERE u.taskStatus = :taskStatus OR :taskStatus IS NULL and u.taskType = :taskType OR :taskType IS NULL and u.taskAllProvinceCode = :taskAllProvinceCode OR :taskAllProvinceCode IS NULL and u.taskAllOCSectionCode = :taskAllOCSectionCode OR :taskAllOCSectionCode IS NULL  and u.taskAllOCRoleCode = :taskAllOCRoleCode OR :taskAllOCRoleCode IS NULL ")
    List<Task> getAllTasks(@Param("taskStatus") String taskStatus,
                              @Param("taskType") String taskType,
                              @Param("taskAllProvinceCode") String taskAllProvinceCode,
                              @Param("taskAllOCSectionCode") String taskAllOCSectionCode,
                              @Param("taskAllOCRoleCode") String taskAllOCRoleCode);


    @Query(value = "SELECT task_seq.nextval FROM dual", nativeQuery =
            true)
    Long getTaskID();

    @Query("SELECT u FROM Task u WHERE u.taskCode = :taskCode and u.taskReferenceCode = :taskReferenceCode and u.taskReferenceType = :taskReferenceType")
    public Task getCloseTask(@Param("taskCode") String taskCode,
                             @Param("taskReferenceCode") String taskReferenceCode,
                             @Param("taskReferenceType") String taskReferenceType);

}