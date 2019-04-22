package com.dw.ngms.cis.workflow.service.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.Processes;
import com.dw.ngms.cis.workflow.service.WorkflowProcessLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessLoaderTest {

	@Autowired
	private WorkflowProcessLoader processLoader;
	
	@Test
	public void testLoadProcesses() throws IOException {
	    Processes value = processLoader.loadProcesses();
	    log.info("Processes size: "+value.getProcessList().size());
	    log.info("First process id: "+value.getProcessList().get(0).getId());
	    assertEquals("infoRequest", value.getProcessList().get(0).getId());
	}//testLoadProcesses
	
	@Test
	public void testGetProcess() throws IOException {
	    Process value = processLoader.getProcess("infoRequest");
	    log.info("Processes id: "+value.getId());
	    assertEquals("infoRequest", value.getId());
	}//testLoadProcesses
}
