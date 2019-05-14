package com.dw.ngms.cis.im.dto;

import com.dw.ngms.cis.workflow.api.ProcessAdditionalInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by ragava on 27/06/17.
 */

@Data
@Getter
@Setter
public class RequestItemsDTO implements Serializable{


    private String gazetteType1;
    private String gazetteType2;
    private String requestCost;
    private String requestHours;
}
