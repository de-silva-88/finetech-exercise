package com.finetech.userauth.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.apachecommons.CommonsLog;

public class MySQLDataSourceHikari {

    public static HikariDataSource getDataSource() {
        return ApplicationDataSource.ds;
    }

    @CommonsLog
    private static class ApplicationDataSource {

        private static HikariDataSource ds;

        static {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://localhost:3306/user_auth");
            config.setUsername("root");
            config.setPassword("scary_bear");
            config.setMinimumIdle(2);
            config.setMaximumPoolSize(10);
            config.setMaxLifetime(20000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(config);
            log.info("Singleton datasource created...");
        }
    }
}
