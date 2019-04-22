package com.dw.ngms.cis.uam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dw.ngms.cis.workflow.model.Processes;
import com.dw.ngms.cis.workflow.service.WorkflowProcessLoader;

@Configuration
public class ProcessConfig {

	@Autowired
	private WorkflowProcessLoader processLoader;
	
	@Bean
	public Processes loadProcesses() {
		return processLoader.loadProcesses();
	}//loadProcesses
	
}
