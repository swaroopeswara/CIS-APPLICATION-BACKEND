package com.dw.ngms.cis.uam.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created by swaroop on 2019/03/26.
 */
@Data
@Setter
@Getter
@ToString
public class ExternalUserAssistantDTO implements Serializable {
    @NotEmpty(message = "Surveyor User Code must not be empty")
    private String surveyorusercode;
    @NotEmpty(message = "Surveyor User Name must not be empty")
    private String surveyorusername;
    @NotEmpty(message = "Assistant User Code not be empty")
    private String assistantusercode;
    @NotEmpty(message = "Assistance User Name not be empty")
    private String assistantusername;
    private String rejectionreason;
    private String isapproved;
}
