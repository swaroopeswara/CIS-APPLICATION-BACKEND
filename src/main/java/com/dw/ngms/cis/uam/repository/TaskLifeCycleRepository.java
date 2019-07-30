package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.TaskLifeCycle;

import java.util.List;

@Repository
public interface TaskLifeCycleRepository extends JpaRepository<TaskLifeCycle, Long> {


    @Query("SELECT u FROM TaskLifeCycle u WHERE u.taskReferenceCode = ?1 order by u.id asc")
    List<TaskLifeCycle> getTasksLifeCycleByTaskReferenceCode(String taskReferenceCode);

}