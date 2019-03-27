package com.dw.ngms.cis.uam.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Created by swaroop on 2019/03/20.
 */
@Data
@Setter
@Getter
public class LoggedUser {
    @NotEmpty(message = "user name must not be empty")
    private String username;
    @NotEmpty(message = "password must not be empty")
    private String password;
    @NotEmpty(message = "internal must not be empty")
    private String internal;

}
