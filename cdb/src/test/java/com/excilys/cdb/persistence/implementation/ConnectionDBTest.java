package com.excilys.cdb.persistence.implementation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class ConnectionDBTest {

    @Test
    public void testGetConnection() {
        try {
            Connection c = ConnectionDB.getConnection();
            assertNotNull(c);
        } catch (SQLException e) {
            fail("SQLException raised");
        }

    }

}
