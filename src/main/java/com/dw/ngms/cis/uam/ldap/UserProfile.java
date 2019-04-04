package com.dw.ngms.cis.uam.ldap;

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
public class UserProfile {

	private String firstName;
	private String lastName;	
	private String designation;
	private boolean isExists = false;

}
