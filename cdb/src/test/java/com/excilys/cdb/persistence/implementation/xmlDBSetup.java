package com.excilys.cdb.persistence.implementation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;

public class xmlDBSetup {

    private final static String COMPANY_QUERY =
            "SELECT * FROM company WHERE id <= 20";
    private final static String COMPUTER_QUERY =
            "SELECT id, name, introduced, discontinued, company_id"
                    + " FROM computer" + " WHERE company_id <= 20"
                    + " ORDER BY id LIMIT 20";

    private static void initXML() throws DatabaseUnitException, SQLException {
        try (Connection jdbcConnection = ConnectionDB.getConnection();) {
            IDatabaseConnection connection =
                    new DatabaseConnection(jdbcConnection);
            DatabaseConfig dbConfig = connection.getConfig();

            // added this line to get rid of the warning
            dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                    new MySqlDataTypeFactory());
            dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
                    new MySqlMetadataHandler());
            dbConfig.setProperty(
                    DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            dbConfig.setProperty(
                    DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
            QueryDataSet dataSet = new QueryDataSet(connection);
            dataSet.addTable("company", COMPANY_QUERY);
//            dataSet.addTable("computer", COMPUTER_QUERY);
            try {
                FlatXmlDataSet.write(dataSet,
                        new FileOutputStream("src/test/resources/dataset.xml"));
//                FlatDtdDataSet.write(connection.createDataSet(),
//                        new FileOutputStream("src/test/resources/dataset.dtd"));
            } catch (DataSetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            initXML();
        } catch (DatabaseUnitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
