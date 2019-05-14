package com.dw.ngms.cis.uam.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swaroop on 2019/03/26.
 */

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserLiteDTO implements Serializable {

	private static final long serialVersionUID = 8918972033622516172L;
	

    private String firstName;
    private String surName;
    private String userName;
    private String userCode;
    private String mobileNumber;
	private String email;
}
