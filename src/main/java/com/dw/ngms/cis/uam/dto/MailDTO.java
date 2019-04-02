package com.dw.ngms.cis.uam.dto;

import lombok.*;

/**
 * Created by swaroop on 2019/03/30.
 */

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MailDTO {

    private String header;
    private String subject;
    private String body;
    private String footer;
    private int id;
    private String toAddress;

}
