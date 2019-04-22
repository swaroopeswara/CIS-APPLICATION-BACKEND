package com.dw.ngms.cis.workflow.api;

import com.dw.ngms.cis.workflow.model.Process;
import java.io.Serializable;

public interface ProcessEngine<T> extends Serializable {

	void startProcess(String processId, T task);
	
	void processUserState(Process process, T task, String currentState, String targetState);
	
	void endProcess(Process process, T task, String currentState);
}
