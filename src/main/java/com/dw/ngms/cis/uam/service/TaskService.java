package com.dw.ngms.cis.uam.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.api.ProcessEngine;
import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.Target;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by swaroop on 2019/04/06.
 */
@Slf4j
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @SuppressWarnings("unused")
	@Autowired
	private UserService userService;
    @Autowired
	private ProcessEngine<Task> processEngine;

    public Task saveTask(Task task) {
        return this.taskRepository.save(task);
    } //FindUserByEmail
    
    public List<Task> findByCriteria(String taskStatus, String taskType, String taskAllProvinceCode, String taskAllOCSectionCode, String taskAllOCRoleCode) {
        return this.taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (taskStatus != null && !StringUtils.isEmpty(taskStatus)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskStatus"), taskStatus)));
                }
                if (taskType != null && !StringUtils.isEmpty(taskType)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskType"), taskType)));
                }
                if (taskAllProvinceCode != null && !StringUtils.isEmpty(taskAllProvinceCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllProvinceCode"), taskAllProvinceCode)));
                }
                if (taskAllOCSectionCode != null && !StringUtils.isEmpty(taskAllOCSectionCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllOCSectionCode"), taskAllOCSectionCode)));
                }
                if (taskAllOCRoleCode != null && !StringUtils.isEmpty(taskAllOCRoleCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllOCRoleCode"), taskAllOCRoleCode)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }//findByCriteria

    public void startProcess(String processId, Requests requests) {
    	if(processId == null)
    		throw new RuntimeException("Process id is reqired to start process");    	
    	processEngine.startProcess(processId, populateTask(requests), populateAdditionalInfo(requests));
    }//startProcess
    
	public Target processUserState(ProcessAdditionalInfo additionalInfo) {
		if(additionalInfo == null)
    		throw new RuntimeException("Task process details reqired to process");
		Task task = getTask(additionalInfo);
		if(task == null) 
    		throw new RuntimeException("Task not found with ID: "+ additionalInfo.getTaskId());
		
    	return processEngine.processUserState(task, additionalInfo);
    }//processUserState
    
    public void endProcess(Process process, Task task, ProcessAdditionalInfo additionalInfo) {
    	processEngine.endProcess(process, task, additionalInfo);
    }//processUserState
    
    public List<Target> getTaskTargetFlows(Long taskId){
    	if(taskId == null)
    		throw new RuntimeException("Task id is reqired");
    	Task task = getTask(taskId);
    	if(task == null) 
    		throw new RuntimeException("Task not found with ID: "+ taskId);
    	
    	return processEngine.getSequenceTargetFlows(task.getTaskType(), task.getTaskStatus());
    }//getTaskTargetFlows
    
    public Task getTask(ProcessAdditionalInfo additionalInfo) {
    	if(additionalInfo == null) return null;    	
    	return (additionalInfo.getTaskId() != null) ? getTask(additionalInfo.getTaskId()) : getTask(additionalInfo.getRequestCode());
    }//getTask
    
    private Task getTask(String requestCode) {
		return (StringUtils.isEmpty(requestCode)) ? null : taskRepository.findByReferenceCode(requestCode);
	}//getTask

	public Task getTask(Long id) {
        return (id == null) ? null : taskRepository.findById(id).get();
    }//getTask

    public Long getTaskID() {
        return this.taskRepository.getTaskID();
    }

    public Task getCloseTask(String taskCode, String taskReferenceCode, String taskReferenceType) {
        return this.taskRepository.getCloseTask(taskCode, taskReferenceCode, taskReferenceType);
    } //FindUserByEmail

    private Task populateTask(Requests requests) {
    	Task task = new Task();
    	Long taskId = this.getTaskID();
        log.info("taskId: {0}", taskId);
        task.setTaskId(taskId);
        task.setTaskCode("TASK000" + Long.toString(taskId));
        task.setCreatedDate(new Date());
        task.setTaskAllProvinceCode(requests.getProvinceCode());
        task.setTaskAllOCSectionCode(requests.getSectionCode());
        task.setTaskOpenDate(new Date());        
        task.setTaskReferenceCode(requests.getRequestCode());
        if(!StringUtils.isEmpty(requests.getUserCode())) {
        	task.setTaskDoneUserCode(requests.getUserCode());
            task.setTaskDoneUserName(requests.getUserName());
        }
		return task;
	}//populateTask

	private ProcessAdditionalInfo populateAdditionalInfo(Requests requests) {
		ProcessAdditionalInfo additionalInfo = new ProcessAdditionalInfo();
		additionalInfo.setProvinceCode(requests.getProvinceCode());
		additionalInfo.setSectionCode(requests.getSectionCode());  
		return additionalInfo;
	}//populateAdditionalInfo

	public String getTaskCurrentStatus(String requestcode) {
		if(StringUtils.isEmpty(requestcode))
    		throw new RuntimeException("Request code is reqired");
		Task task = getTask(requestcode);
    	if(task == null) 
    		throw new RuntimeException("Task not found with request code: "+ requestcode);		
		
		return task.getTaskStatus();
	}//getTaskCurrentStatus

}
