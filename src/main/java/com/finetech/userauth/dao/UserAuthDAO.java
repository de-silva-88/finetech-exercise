package com.finetech.userauth.dao;

import static com.finetech.jooq.tables.User.USER;
import com.finetech.userauth.controller.domain.Credentials;
import com.finetech.userauth.db.MySQLConn;
import com.finetech.userauth.exception.DataAccessException;
import java.io.IOException;
import java.util.List;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class UserAuthDAO {

    public boolean addUser(Credentials credentials) {
        log.info("Adding new user");
        try (MySQLConn conn = new MySQLConn()) {
            int insertResult = conn.getDSLContext()
                    .insertInto(USER, USER.NAME, USER.PASSWD_HASH)
                    .values(credentials.getUserName(), credentials.getPasswordHash())
                    .execute();
            log.info("insert result = " + insertResult);
            return (insertResult == 0 ? false: true);
        } catch (IOException | DataAccessException ex) {
            log.error("Error getting mysql conneciton or mapping error occured. Operation terminated. Exiting with false. Message : {}" + ex.getMessage());
            return false;
        }
    }
    
    public String getUserPwhashByName(String userName) {
        log.info("Getting user-pw-hash by username : " + userName);
        try (MySQLConn conn = new MySQLConn()) {
            List<String> userPwHashAsList = conn.getDSLContext()
                    .select(USER.PASSWD_HASH)
                    .from(USER)
                    .where(USER.NAME.equal(userName))
                    .fetch().into(String.class);
            if(userPwHashAsList == null ? true : userPwHashAsList.isEmpty()) return null;
            return userPwHashAsList.get(0);
        } catch (IOException | DataAccessException ex) {
            log.error("Error getting mysql conneciton or mapping error occured. Operation terminated. Exiting with null. Message : {}" + ex.getMessage());
            return null;
        }
    }
    
    public boolean isUserAlreadyExists(String userName) {
        log.info("Getting user by username : " + userName);
        try (MySQLConn conn = new MySQLConn()) {
            List<Integer> userPwHashAsList = conn.getDSLContext()
                    .select(USER.ID)
                    .from(USER)
                    .where(USER.NAME.equal(userName))
                    .fetch().into(Integer.class);
            if(userPwHashAsList == null ? true : userPwHashAsList.isEmpty()) return false;
            return true;
        } catch (IOException | DataAccessException ex) {
            log.error("Error getting mysql conneciton or mapping error occured. Operation terminated. Exiting with null. Message : {}" + ex.getMessage());
            return false;
        }
    }
}
