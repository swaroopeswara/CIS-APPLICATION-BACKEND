package com.dw.ngms.cis.uam.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * Created by swaroop on 2019/03/26.
 */

@Data
@Getter
@Setter
@ToString
public class UserDTO implements Serializable {

    @NotEmpty(message = "User Code must not be empty")
    private String usercode;
    @NotEmpty(message = "User Name must not be empty")
    private String username;
    private String isapproved;
    private String rejectionreason;
    private String isactive;
    private List<SecurityQuestionDTO> question;

}
