package com.excilys.cdb.persistence.implementation;

import java.sql.Connection;
import java.sql.SQLException;

import com.excilys.cdb.persistence.interfaces.SQLDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCP implements SQLDataSource {

    private static final HikariDataSource DATASOURCE;

    static {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost/computer-database-db"
                + "?serverTimezone=UTC");
        config.setUsername("admincdb");
        config.setPassword("qwerty1234");
        DATASOURCE =
                new HikariDataSource(new HikariConfig("/hikari.properties"));

    }

    @Override
    public Connection getConnection() throws SQLException {
        return DATASOURCE.getConnection();
    }

}
