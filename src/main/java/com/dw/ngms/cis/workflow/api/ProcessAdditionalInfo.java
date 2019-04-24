package com.dw.ngms.cis.workflow.api;

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
public class ProcessAdditionalInfo {

	private String provinceCode;
	private String sectionCode;
	private String currentState;
	private String targetState;
}
