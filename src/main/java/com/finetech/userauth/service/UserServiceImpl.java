package com.finetech.userauth.service;

import com.finetech.userauth.controller.domain.Credentials;
import com.finetech.userauth.dao.UserAuthDAO;
import com.finetech.userauth.exception.DataProcessingException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class UserServiceImpl implements UserService {

    private UserAuthDAO dao = new UserAuthDAO();

    /*
     * add user to the db upon registration
     */
    @Override
    public boolean addUserService(Credentials credentials) {
        log.info("Adding user operation initiated ...");
        String pwHash;
        try {
            pwHash = getHash(credentials.getPassword());
        } catch (DataProcessingException ex) {
            return false;
        }
        log.info("created digest : " + pwHash);
        credentials.setPasswordHash(pwHash);
        boolean userAlreadyExists = dao.isUserAlreadyExists(credentials.getUserName());
        // if user already exists, credentials will not be saved in db,
        // but will be considered as registering operation is successful
        if (userAlreadyExists) {
            log.warn("User already exists. Skipping db operation");
            return true;
        }
        return dao.addUser(credentials);
    }

    /*
     * check if the user exists by name.
     * check the hash of the given password against db-stored password hash
     */
    @Override
    public boolean authenticateUser(Credentials credentials) {
        String userPwhashStored = dao.getUserPwhashByName(credentials.getUserName());
        // if the password hash is not being able to retrieve, consider operation as failed
        if (userPwhashStored == null) {
            log.error("Unable to authenticate user.");
            return false;
        }
        String userPwHashSent;
        try {
            userPwHashSent = getHash(credentials.getPassword());
        } catch (DataProcessingException ex) {
            log.error("Unable to authenticate user.");
            // if the unable to create password hash from given password, consider operation as failed
            return false;
        }
        log.info("stored : " + userPwhashStored);
        log.info("sent   : " + userPwHashSent);
        return userPwhashStored.equals(userPwHashSent);
    }

    /*
     * Create md5-hash from given string (password)
     */
    private String getHash(String password) throws DataProcessingException {
        try {
            byte[] bytesOfMessage = password.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytesOfMessage);
            return new String(digest);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            log.error("Error creating hash of the password. Reason : " + ex.getMessage());
            throw new DataProcessingException("Error creating hash of the password");
        }
    }
}
