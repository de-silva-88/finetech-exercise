package com.finetech.userauth.controller.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Credentials {

    private String userName;
    private String password;
    private String passwordHash;
    private String email;
}
