package com.dw.ngms.cis.workflow.model;

import javax.xml.bind.annotation.XmlRootElement;

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
public class Target {

	@JsonProperty("id")
	private String id;
	@JsonProperty("restRequest")
	private String restRequest;
	@JsonProperty("description")
	private String description;
		
}
