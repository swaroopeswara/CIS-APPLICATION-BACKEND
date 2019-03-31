package com.dw.ngms.cis.uam.enums;

public enum Status {

	Y("Approved"),
	N("Waiting"),
	PND("Pending");

	String status;
	
	private Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
