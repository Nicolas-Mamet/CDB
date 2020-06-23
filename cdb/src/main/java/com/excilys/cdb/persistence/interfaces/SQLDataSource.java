package com.excilys.cdb.persistence.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLDataSource {
    Connection getConnection() throws SQLException;
}
