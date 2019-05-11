package com.dw.ngms.cis.workflow.api;

import java.util.List;

import com.dw.ngms.cis.workflow.model.Assignee;

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

	private Long taskId;
	private String requestCode;
	private String provinceCode;
	private String sectionCode;
	private String targetSequenceId;
	private String userCode;
	private String userName;
	List<Assignee> assigneeList;
	private String url;//Not required to be passed
}
