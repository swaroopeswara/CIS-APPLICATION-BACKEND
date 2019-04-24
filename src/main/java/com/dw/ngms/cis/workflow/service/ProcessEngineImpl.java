package com.dw.ngms.cis.workflow.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.repository.InternalRoleRepository;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import com.dw.ngms.cis.workflow.api.ProcessEngine;
import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.Processes;
import com.dw.ngms.cis.workflow.model.SequenceFlow;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessEngineImpl implements ProcessEngine<Task>{

	private static final long serialVersionUID = -1376251071755842434L;

	@Autowired
	private Processes processes;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private InternalRoleRepository internalRoleRepository;
	
	@Override
	public void startProcess(String processId, Task task, ProcessAdditionalInfo additionalInfo) {
		if(processes == null) {
			log.error("processes not initialised");
			return;
		}
		Process process = processes.getProcess(processId);
		if(process == null) {
			log.error("process not found");
			return;
		}		
		updateProvinceAndSectionCodes(task, additionalInfo);
		SequenceFlow targetSequence = process.getSequenceFlow(process.getStartSequenceFlow().getTargetList().get(0).getId());
		if(targetSequence != null) {
			task.setTaskStatus(targetSequence.getState());
			updateRoleAndUserAssociations(task, additionalInfo, targetSequence);
		}		
		taskRepository.save(task);
		addLifeCycleEntry(task, additionalInfo);
	}//startProcess

	@Override
	public void processUserState(Process process, Task task, ProcessAdditionalInfo additionalInfo) {
		if(process == null) {
			log.error("process not found");
			return;
		}
		updateProvinceAndSectionCodes(task, additionalInfo);
//		SequenceFlow sequence = process.getSequenceFlow(currentState);
		SequenceFlow targetSequence = process.getSequenceFlow(additionalInfo.getTargetState());
		if(targetSequence != null) {
			task.setTaskStatus(targetSequence.getState());
			//check dependancy
			validateDependancy(task, targetSequence);
			updateRoleAndUserAssociations(task, additionalInfo, targetSequence);
		}
		taskRepository.save(task);
		addLifeCycleEntry(task, additionalInfo);
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

	private void addLifeCycleEntry(Task task, ProcessAdditionalInfo additionalInfo) {
		//FIXME need to add life cycle		
	}//addLifeCycleEntry

	private void updateRoleAndUserAssociations(Task task, ProcessAdditionalInfo additionalInfo,
			SequenceFlow targetSequence) {
		
		Set<InternalRole> roleList = new HashSet<>();
		Set<User> userList = new HashSet<>();
		
		if(!CollectionUtils.isEmpty(targetSequence.getAssigneeList())) {
			targetSequence.getAssigneeList().forEach(assignee -> {
				if("Role".equalsIgnoreCase(assignee.getType())) {
					roleList.addAll(internalRoleRepository.findByProvinceCodeAndSectionCodeAndRoleName(additionalInfo.getProvinceCode(), 
							additionalInfo.getSectionCode(), assignee.getName()));						
				} else {
//					userList.addAll('TODO');//FIXME
				}
			});			
		}
		//clear all user and role associations
		clearRoleAndUserAssociations(task);
		
		if(!CollectionUtils.isEmpty(roleList)) {			
			roleList.forEach(role -> {
				task.getInternalRoleList().add(role);
			});
		}//if roleList
		if(!CollectionUtils.isEmpty(userList)) {
			userList.forEach(user -> {
				task.getUserList().add(user);
			});
		}//if userList
	}//updateRoleAndUserAssociations

	private void updateProvinceAndSectionCodes(Task task, ProcessAdditionalInfo additionalInfo) {
		if(additionalInfo != null) {
			if(!StringUtils.isEmpty(additionalInfo.getProvinceCode()))
				task.setTaskAllProvinceCode(additionalInfo.getProvinceCode());
			if(!StringUtils.isEmpty(additionalInfo.getSectionCode()))
				task.setTaskAllOCSectionCode(additionalInfo.getSectionCode());
		}
	}//updateProvinceAndSectionCodes

	private void clearRoleAndUserAssociations(Task task) {
		if(task == null) return;
		task.getInternalRoleList().clear();
		task.getUserList().clear();
	}//clearRoleAndUserAssociations

	private void validateDependancy(Task task, SequenceFlow targetSequence) {
		String urlString = "http://localhost:8089//cisorigin.uam/api/v1/";
		if(task == null || task.getTaskId() == null || targetSequence == null || 
				StringUtils.isEmpty(targetSequence.getRestDependancy())){
			return;
		}
		urlString = urlString.concat(targetSequence.getRestDependancy()).concat(task.getTaskId().toString());	
		HttpURLConnection httpCon = null;
		try {
			URL url = new URL(urlString);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setRequestMethod("GET");
			int status = httpCon.getResponseCode();
			log.info("status : {0}", status);
			//FIXME need to work on it
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if(httpCon != null) httpCon.disconnect();
		}
	}//validateDependancy

}
