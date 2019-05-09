package com.dw.ngms.cis.uam.enums;

public enum LapseStatus {

	NONE("NONE"),
	PRELAPSE("PRELAPSE"),
	LAPSED("LAPSED");

	String displayString;
	
	private LapseStatus(String displayString) {
		this.displayString = displayString;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}
}
