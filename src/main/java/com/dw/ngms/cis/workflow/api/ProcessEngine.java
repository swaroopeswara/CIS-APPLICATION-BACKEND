package com.dw.ngms.cis.workflow.api;

import com.dw.ngms.cis.workflow.model.Process;
import java.io.Serializable;

public interface ProcessEngine<T> extends Serializable {

	void startProcess(String processId, T task, ProcessAdditionalInfo additionalInfo);
	
	void processUserState(Process process, T task, ProcessAdditionalInfo additionalInfo);
	
	void endProcess(Process process, T task, ProcessAdditionalInfo additionalInfo);
}
