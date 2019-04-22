package com.dw.ngms.cis.workflow.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;

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
public class Process {	
	
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("state")
	private String state;
	@JsonProperty("sequenceFlowList")
	private List<SequenceFlow> sequenceFlowList = new ArrayList<SequenceFlow>();
	
	public SequenceFlow getSequenceFlow(String sequenceFlowId) {
		return (!CollectionUtils.isEmpty(sequenceFlowList)) ? 
				sequenceFlowList.stream().filter(p -> (p.getId() != null) && 
						p.getId().equals(sequenceFlowId)).findFirst().get() : null;
	}//getSequenceFlow
	
	public SequenceFlow getSequenceFlowByName(String name) {
		return (!CollectionUtils.isEmpty(sequenceFlowList)) ? 
			sequenceFlowList.stream().filter(s -> (s.getName() != null) && 
					s.getName().equals(name)).findFirst().get() : null;
	}//getSequenceFlow
	
	public SequenceFlow getStartSequenceFlow() {
		return getSequenceFlowByName("theStart");
	}//getSequenceFlow
	
	public SequenceFlow getEndSequenceFlow() {
		return getSequenceFlowByName("theEnd");
	}//getSequenceFlow
}
