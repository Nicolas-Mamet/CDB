package com.excilys.cdb.persistence.implementation;

import java.sql.Connection;
import java.sql.SQLException;

import com.excilys.cdb.persistence.interfaces.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCP implements DataSource {

    private final HikariDataSource datasource;

    public HikariCP(String config) {
        System.out.println("HikariCP démarre");
        this.datasource = new HikariDataSource(new HikariConfig(config));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

}
