package com.dw.ngms.cis.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.repository.TaskRepository;
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
	
	@Override
	public void startProcess(String processId, Task task) {
		if(processes == null) {
			log.error("processes not initialised");
			return;
		}
		Process process = processes.getProcess(processId);
		if(process == null) {
			log.error("process not found");
			return;
		}
				
		SequenceFlow targetSequence = process.getSequenceFlow(process.getStartSequenceFlow().getTargetList().get(0).getId());
		task.setTaskStatus(targetSequence.getState());
		task.setTaskAllOCRoleCode(targetSequence.getAssigneeList().get(0).getName());//TODO need to fix by type
		//need to discuss abt province and section
		taskRepository.save(task);
		//TODO need to add life cycle
	}//startProcess

	@Override
	public void processUserState(Process process, Task task, String currentState, String targetState) {
		if(process == null) {
			log.error("process not found");
			return;
		}
//		SequenceFlow sequence = process.getSequenceFlow(currentState);
		SequenceFlow targetSequence = process.getSequenceFlow(targetState);
		task.setTaskStatus(targetSequence.getState());
		task.setTaskAllOCRoleCode(targetSequence.getAssigneeList().get(0).getName());

		//need to discuss abt province and section
		taskRepository.save(task);		
	}//processUserState

	@Override
	public void endProcess(Process process, Task task, String currentState) {
		if(process == null) {
			log.error("process not found");
			return;
		}
		SequenceFlow sequence = process.getEndSequenceFlow();
		task.setTaskStatus(sequence.getState());
		//need to discuss abt province and section
		taskRepository.save(task);
	}

}
