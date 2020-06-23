package com.excilys.cdb.persistence.implementation;

import java.sql.Connection;
import java.sql.SQLException;

import com.excilys.cdb.persistence.interfaces.SQLDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public final class ConnectionDB implements SQLDataSource {

    private static final String URL =
            "jdbc:mysql://localhost/computer-database-db";

    private static final String USERNAME = "admincdb";

    private static final String PASSWORD = "qwerty1234";

    private static final MysqlDataSource DATASOURCE = new MysqlDataSource();

    static {
        DATASOURCE.setUrl(URL);
        DATASOURCE.setUser(USERNAME);
        DATASOURCE.setPassword(PASSWORD);
        try {
            DATASOURCE.setServerTimezone("UTC");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DATASOURCE.getConnection();
    }
}
