package com.dw.ngms.cis.uam.enums;

public enum ApprovalStatus {

	YES("YES"),
	WAITING("Waiting"),
	PENDING("Pending");

	String displayString;
	
	private ApprovalStatus(String displayString) {
		this.displayString = displayString;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
	
}
