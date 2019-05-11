package com.dw.ngms.cis.uam.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by swaroop on 2019/03/30.
 */

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MailDTO {

    private String mailFrom;

    private String mailTo;

    private String mailCc;

    private String mailBcc;

    private String mailSubject;

    private String mailContent;

    private String contentType;

    private List<Object> attachments = new ArrayList<>();

    private Map<String, Object> model = new HashMap<String, Object>();

    private List<String> mailsTo = new ArrayList<>();
    
    private Map<String, String> userNameMap = new HashMap<String, String>();
    
    private String header;
    private String subject;
    private String body1;
    private String body2;
    private String body3;
    private String body4;
    private String footer;
    private int id;
    private String toAddress;

}
