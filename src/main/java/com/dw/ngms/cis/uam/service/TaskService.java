package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.ExternalUser;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;



    public Task saveTask(Task task) {
        return this.taskRepository.save(task);
    } //FindUserByEmail


    public List<Task> getAllTasks(String taskStatus,String taskType,String taskAllProvinceCode,String taskAllOCSectionCode,String taskAllOCRoleCode){
        return this.taskRepository.getAllTasks(taskStatus,taskType,taskAllProvinceCode,taskAllOCSectionCode,taskAllOCRoleCode);
    }

    public Long getTaskID() {
        return this.taskRepository.getTaskID();
    }

    public Task getCloseTask(String taskCode, String taskReferenceCode, String taskReferenceType) {
        return this.taskRepository.getCloseTask(taskCode,taskReferenceCode,taskReferenceType);
    } //FindUserByEmail



}
