package com.excilys.cdb.persistence.implementation;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.junit.Test;

import com.excilys.cdb.model.Company;

public class CompanyDAOImplTestH2 extends DBTestCase {

    private static final String COMPANY_LIST_QUERY =
            "SELECT id, name FROM company ORDER BY id";
    private static final String COMPANY_INSERT_QUERY =
            "INSERT INTO company (id, name) VALUES (21, 'BOB')";

    public CompanyDAOImplTestH2(String name) {
        super(name);
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                "org.h2.Driver");
        System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=runscript "
                        + "from 'src/test/resources/SCHEMA-TEST.sql'");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
                "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
                "");
//        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
//                "test-db");
    }

//    @Override
//    protected DatabaseOperation getSetUpOperation() throws Exception {
//        return DatabaseOperation.CLEAN_INSERT;
//    }
//
//    @Override
//    protected DatabaseOperation getTearDownOperation() throws Exception {
//        return DatabaseOperation.NONE;
//    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
//        config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
//                new MySqlMetadataHandler());
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                new H2DataTypeFactory());
//        config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, false);
//        config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES,
//                true);
    }

    @Test
    public void testGetCompanies() throws Exception {
        try {
            List<Company> companies = new CompanyDAOImpl().getCompanies();
            assertEquals(20, companies.size());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail("SQLException raised");
        }
//        ITableIterator it = getDataSet().iterator();
//        while (it.next()) {
//            System.out.println("bob1");
//            System.out.println(it.getTable().getRowCount());
//        }
//        FileInputStream in =
//                new FileInputStream("src/test/resources/dataset.xml");
//        Scanner scan = new Scanner(in);
//        while (scan.hasNextLine()) {
//            System.out.println(scan.nextLine());
//        }
//        scan.close();
//        try (Connection connection = getConnection().getConnection();
//                Statement statement = connection.createStatement();) {
//            System.out.println(statement.executeUpdate(COMPANY_INSERT_QUERY));
//            connection.commit();
//        }
//
//        IDataSet dataSet = getConnection().createDataSet();
//        ITableIterator it = dataSet.iterator();
//        while (it.next()) {
//            System.out.println(it.getTableMetaData().getTableName());
//            System.out.println(it.getTable().getRowCount());
//        }
//
//        DatabaseOperation.INSERT.execute(getConnection(), getDataSet());
//        List<Company> companies = new ArrayList<>();
//
//        try (Connection connection = getConnection().getConnection();
//                Statement statement = connection.createStatement();
//                ResultSet resultSet =
//                        statement.executeQuery(COMPANY_LIST_QUERY);) {
//            while (resultSet.next()) {
//                System.out.println("bob");
//                Company company;
//                company = Company.builder().withID(resultSet.getInt("id"))
//                        .withName(resultSet.getString("name")).build();
//
//                companies.add(company);
//            }
//        }
//        System.out.println(companies.size());
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
