package com.excilys.cdb.persistence.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
    Connection getConnection() throws SQLException;
}
