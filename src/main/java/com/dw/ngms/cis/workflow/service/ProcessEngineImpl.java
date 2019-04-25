package com.dw.ngms.cis.workflow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
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
import com.dw.ngms.cis.uam.repository.InternalRoleRepository;
import com.dw.ngms.cis.uam.repository.TaskLifeCycleRepository;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import com.dw.ngms.cis.uam.repository.UserRepository;
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
	private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskLifeCycleRepository lifeCycleRepository;
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
			validateDependancy(task, targetSequence, additionalInfo.getUrl());
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
		//FIXME update task properties		
		TaskLifeCycle lifeCycleEntity = new TaskLifeCycle();
//		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
		BeanUtils.copyProperties(task, lifeCycleEntity, "createdDate");
		lifeCycleRepository.saveAndFlush(lifeCycleEntity);
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
					userList.add(userRepository.findByLoginName(assignee.getName()));
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
