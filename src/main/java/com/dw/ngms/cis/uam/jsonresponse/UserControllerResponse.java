package com.dw.ngms.cis.uam.jsonresponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by swaroop on 2019/03/21.
 */

@Data
@Getter
@Setter
public class UserControllerResponse {

    private String exists;
    private String valid;

}
