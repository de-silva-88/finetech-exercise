package com.finetech.userauth.service;

import com.finetech.userauth.controller.domain.Credentials;

public interface UserService {
    
    boolean addUserService (Credentials credentials);
    
    boolean authenticateUser (Credentials credentials);
}
