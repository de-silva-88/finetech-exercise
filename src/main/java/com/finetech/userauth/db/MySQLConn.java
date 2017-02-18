package com.finetech.userauth.db;

import com.finetech.userauth.exception.DataAccessException;
import com.zaxxer.hikari.HikariDataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.apachecommons.CommonsLog;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

@CommonsLog
public class MySQLConn implements Closeable {

    private Connection connection;

    public MySQLConn() {
        try {
            HikariDataSource dataSource = MySQLDataSourceHikari.getDataSource();
            log.debug("Getting conneciton from datasource..");
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Error getting connection from hikari data-source. Message : {}" + e.getMessage());
            connection = null;
        }
    }

    public DSLContext getDSLContext() throws DataAccessException {
        if (connection == null) throw new DataAccessException(
                "Unable to create DSL Context as the connetion creations is unsuccessful.");
        log.debug("Create DSL context from connection");
        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
        return create;
    }

    @Override
    public void close() throws IOException {
        try {
            log.debug("Closing mysql connection...");
            connection.close();
        } catch (SQLException ex) {
            log.error("Error closing mysql conneciton. Refer to the stack trace. Message : {}" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
