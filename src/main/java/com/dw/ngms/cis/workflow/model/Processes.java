package com.dw.ngms.cis.workflow.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Processes {
	
	@JsonProperty("processList")
	private List<Process> processList;

	public Process getProcess(String processId) {
		return (!CollectionUtils.isEmpty(processList)) ? 
			processList.stream().filter(p -> (p.getId() != null)).findFirst().get() : null;
	}//getProcess
}
