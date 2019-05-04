package com.dw.ngms.cis.uam.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

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

    private List< Object > attachments;

    private Map< String, Object > model;

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
