package com.dw.ngms.cis.workflow.api;

import java.io.Serializable;
import java.util.List;

import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.SequenceFlow;
import com.dw.ngms.cis.workflow.model.Target;

public interface ProcessEngine<T> extends Serializable {

	void startProcess(String processId, T task, ProcessAdditionalInfo additionalInfo);
	
	Target processUserState(Task task, ProcessAdditionalInfo additionalInfo);
	
	void endProcess(Process process, T task, ProcessAdditionalInfo additionalInfo);
	
	Target getTargetByIdAndProcessId(String targetId, String processId);
	
	List<Target> getSequenceTargetFlows(String processCode, String state);

	Process getProcessById(String processId);

	SequenceFlow getSequenceByStateAndProcessId(String state, String processId);
}
