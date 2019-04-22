package com.dw.ngms.cis.workflow.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import com.dw.ngms.cis.workflow.model.Process;
import com.dw.ngms.cis.workflow.model.Processes;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WorkflowProcessLoader {

	public Processes loadProcesses() {
		try {
			InputStream processStream = this.getClass().getResourceAsStream("/process/".concat("cis-process.xml"));
			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);	    
			return xmlMapper.readValue(processStream, Processes.class);
		} catch (JsonParseException e) {
			log.error("Json parser error: "+e.getMessage());
		} catch (JsonMappingException e) {
			log.error("Json mapping error: "+e.getMessage());
		} catch (IOException e) {
			log.error("Xml IO error: "+e.getMessage());
		}	
		return null;
	}//loadProcesses
	
	public Process getProcess(String processId) {
		Processes processes = loadProcesses();
		return (processes!= null) ? processes.getProcess(processId) : null;
	}//getProcess

	@Test
	public void testLoadProcesses() throws IOException {
	    Processes value = loadProcesses();
	    log.info("Processes size: "+value.getProcessList().size());
	    log.info("First process id: "+value.getProcessList().get(0).getId());
	    assertEquals("infoRequest", value.getProcessList().get(0).getId());
	}//testLoadProcesses
	
	@Test
	public void testGetProcess() throws IOException {
	    Process value = getProcess("infoRequest");
	    log.info("Processes id: "+value.getId());
	    assertEquals("infoRequest", value.getId());
	}//testLoadProcesses
}