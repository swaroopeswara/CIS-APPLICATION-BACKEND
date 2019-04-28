package com.dw.ngms.cis.workflow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.TaskLifeCycle;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.TaskLifeCycleRepository;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import com.dw.ngms.cis.uam.repository.UserRepository;
import com.dw.ngms.cis.uam.service.InternalRoleService;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.api.ProcessEngine;
import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.Processes;
import com.dw.ngms.cis.workflow.model.SequenceFlow;
import com.dw.ngms.cis.workflow.model.Target;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessEngineImpl implements ProcessEngine<Task>{

	private static final long serialVersionUID = -1376251071755842434L;

	@Autowired
	private Processes processes;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskLifeCycleRepository lifeCycleRepository;
	@Autowired
	private InternalRoleService internalRoleService;	
	
	@Override
	public void startProcess(String processId, Task task, ProcessAdditionalInfo additionalInfo) {		
		Process process = getProcessById(processId);
		if(process == null) {
			log.error("process not found");
			return;
		}		
//		updateTaskDetails(task, additionalInfo);
		task.setTaskOpenDesc(process.getDescription());
		task.setTaskType(process.getId());
		SequenceFlow targetSequence = process.getSequenceFlow(process.getStartSequenceFlow().getTargetList().get(0).getId());
		if(targetSequence != null) {
			task.setTaskStatus(targetSequence.getState());
			updateRoleAndUserAssociations(task, additionalInfo, targetSequence);
		}		
		taskRepository.save(task);
		addLifeCycleEntry(task, additionalInfo);
	}//startProcess

	@Override
	public Target processUserState(Task task, ProcessAdditionalInfo additionalInfo) {
		if(task == null || additionalInfo == null) {
			log.error("Task details required to process");
			return null;
		}
		Process process = getProcessById(task.getTaskType());
		if(process == null) {
			log.error("process not found");
			return null;
		}
		SequenceFlow targetSequence = process.getSequenceFlow(additionalInfo.getTargetSequenceId());		
		if(targetSequence != null) {
			updateTaskDetails(task, additionalInfo);			
			task.setTaskStatus(targetSequence.getState());
			//check dependancy not required
//			validateDependancy(task, targetSequence, additionalInfo.getUrl());
			updateRoleAndUserAssociations(task, additionalInfo, targetSequence);
		}
		taskRepository.save(task);
		addLifeCycleEntry(task, additionalInfo);
		return getTargetByIdAndProcessId(additionalInfo.getTargetSequenceId(), task.getTaskType());
	}//processUserState

	@Override
	public void endProcess(Process process, Task task, ProcessAdditionalInfo additionalInfo) {
		if(process == null) {
			log.error("process not found");
			return;
		}
		SequenceFlow sequence = process.getEndSequenceFlow();
		task.setTaskStatus(sequence.getState());
		taskRepository.save(task);
		addLifeCycleEntry(task, additionalInfo);
	}//endProcess

	public Target getTargetByIdAndProcessId(String targetId, String processId) {
		if(targetId == null) {
			log.error("Target id is required");
			return null;
		}
		Process process = getProcessById(processId);
		if(process == null) {
			log.error("process not found");
			return null;
		}
		for(SequenceFlow sequence: process.getSequenceFlowList()){	
			if(sequence != null) {
				for(Target target :sequence.getTargetList()){
					if(targetId.equals(target.getId()))
						return target;
				}//inner 
			}//end if
		}//outer
		return null;
	}//getTargetByIdAndProcessId
	
	@Override
	public List<Target> getSequenceTargetFlows(String processCode, String state){
		if(StringUtils.isEmpty(processCode)) 
    		throw new RuntimeException("Task process name not found");
    	if(StringUtils.isEmpty(state)) 
    		throw new RuntimeException("Task status not found");
    	SequenceFlow sequence = getSequenceByStateAndProcessId(state, processCode);
    	if(sequence == null) 
    		throw new RuntimeException("Sequence not found with status"+state +"in process "+processCode);
    	
    	return sequence.getTargetList();
	}//getSequenceTargetFlows
	
	@Override
	public Process getProcessById(String processId) {
		if(processes == null) {
			log.error("processes not initialised");
			return null;
		}
		return processes.getProcess(processId);
	}//getProcessById
	
	@Override
	public SequenceFlow getSequenceByStateAndProcessId(String state, String processId) {		
		Process process = getProcessById(processId);
		if(process == null) {
			log.error("process not found");
			return null;
		}
		return process.getSequenceFlowByState(state);
	}//getSequenceByStateAndProcessId
	
	private void addLifeCycleEntry(Task task, ProcessAdditionalInfo additionalInfo) {
		TaskLifeCycle lifeCycleEntity = new TaskLifeCycle();
//		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
		BeanUtils.copyProperties(task, lifeCycleEntity, "createdDate");
		lifeCycleRepository.saveAndFlush(lifeCycleEntity);
	}//addLifeCycleEntry

	private void updateRoleAndUserAssociations(Task task, ProcessAdditionalInfo additionalInfo,
			SequenceFlow targetSequence) {
		if(targetSequence == null)
			return;
		
		Set<InternalRole> roleList = new HashSet<>();
		Set<User> userList = new HashSet<>();
		
		if(!CollectionUtils.isEmpty(targetSequence.getAssigneeList())) {
			targetSequence.getAssigneeList().forEach(assignee -> {
				if("Role".equalsIgnoreCase(assignee.getType())) {
					List<InternalRole> roles = internalRoleService.findByProvinceCodeAndSectionCodeAndRoleName(additionalInfo.getProvinceCode(), 
							additionalInfo.getSectionCode(), assignee.getName());
					if(!CollectionUtils.isEmpty(roles))
						roleList.addAll(roles);						
				} else {
					userList.add(userRepository.findByLoginName(assignee.getName()));
				}
			});			
		}
		//clear all user and role associations and reassign		
		if(!CollectionUtils.isEmpty(roleList)) {	
			task.getInternalRoleList().clear();
			
			roleList.forEach(role -> {
				task.getInternalRoleList().add(role);
			});
		}//if roleList
		if(!CollectionUtils.isEmpty(userList)) {
			task.getUserList().clear();
			
			userList.forEach(user -> {
				task.getUserList().add(user);
			});
		}//if userList
		if("theEnd".equalsIgnoreCase(targetSequence.getName())) {
			//clear all user and role associations 
			clearRoleAndUserAssociations(task);
		}
	}//updateRoleAndUserAssociations

	private void updateTaskDetails(Task task, ProcessAdditionalInfo additionalInfo) {
		if(additionalInfo != null) {
			if(!StringUtils.isEmpty(additionalInfo.getProvinceCode()))
				task.setTaskAllProvinceCode(additionalInfo.getProvinceCode());
			if(!StringUtils.isEmpty(additionalInfo.getSectionCode()))
				task.setTaskAllOCSectionCode(additionalInfo.getSectionCode());
			if(!StringUtils.isEmpty(additionalInfo.getUserCode()))
				task.setTaskDoneUserCode(additionalInfo.getUserCode());
			if(!StringUtils.isEmpty(additionalInfo.getUserName()))
				task.setTaskDoneUserName(additionalInfo.getUserName());
		}
	}//updateTaskDetails

	private void clearRoleAndUserAssociations(Task task) {
		if(task == null) return;
		task.getInternalRoleList().clear();
		task.getUserList().clear();
	}//clearRoleAndUserAssociations

	private void validateDependancy(Task task, SequenceFlow targetSequence, String url) {
		String urlString = "cisorigin.uam/api/v1/";
		if(task == null || task.getTaskId() == null || targetSequence == null || 
				StringUtils.isEmpty(targetSequence.getRestDependancy())){
			return;
		}
		boolean isDependancyCompleted = false;
		url = url.concat(urlString).concat(targetSequence.getRestDependancy()).concat(task.getTaskId().toString());	
		CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();	 
            if (status >= 200 && status < 300) {
                BufferedReader br;	                 
                br = new BufferedReader(new InputStreamReader(response
                            .getEntity().getContent()));
	                 
                String line = "";
                while ((line = br.readLine()) != null) {
                	log.info(line);
                }
                isDependancyCompleted = true;
            } else {
               log.info("Unexpected response status: {0}", status);
            }
        } catch (IOException | UnsupportedOperationException e) {
        	log.error("Error: {0}", e.getMessage());
        } finally {
            if(null != response){
                try {
                    response.close();
                    client.close();
                } catch (IOException e) {
                	log.error("Error: {0}", e.getMessage());
                }
            }
        }
        if(!isDependancyCompleted)
        	throw new RuntimeException("Task dependancy is in pending.");
	}//validateDependancy
 
}
