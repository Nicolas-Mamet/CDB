package com.excilys.cdb.persistence.implementation;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import com.excilys.cdb.model.Company;

public class CompanyDAOImplTest extends DBTestCase {

    private static final String COMPANY_LIST_QUERY =
            "SELECT id, name FROM company ORDER BY id";

    public CompanyDAOImplTest(String name) {
        super(name);
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "com.mysql.cj.jdbc.Driver");
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:mysql://localhost/test-db?serverTimezone=UTC");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
                "admincdb");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
                "qwerty1234");
//        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
//                "test-db");
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.CLEAN_INSERT;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
                new MySqlMetadataHandler());
        config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, false);
        config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES,
                true);
    }

    @Test
    public void testGetCompanies() throws Exception {
//        try {
////            List<Company> companies = new CompanyDAOImpl().getCompanies();
////            assertEquals(20, companies.size());
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            fail("SQLException raised");
//        }
        List<Company> companies = new ArrayList<>();

        try (Connection connection = getConnection().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery(COMPANY_LIST_QUERY);) {
            while (resultSet.next()) {
                companies.add(Company.builder().withID(resultSet.getInt("id"))
                        .withName(resultSet.getString("name")).build());
            }
        }
        System.out.println(companies.size());
    }
//
//    public void testCheckCompany() {
//        // fail("Not yet implemented");
//    }
//
//    public void testGetPageOfCompanies() {
//        // fail("Not yet implemented");
//    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder()
                .build(new FileInputStream("src/test/resources/dataset.xml"));
    }

}
