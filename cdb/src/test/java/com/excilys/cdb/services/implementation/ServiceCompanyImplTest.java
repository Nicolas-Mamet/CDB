package com.excilys.cdb.services.implementation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.implementation.DAOFactory;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.services.interfaces.ServiceCompany;

@RunWith(MockitoJUnitRunner.class)
public class ServiceCompanyImplTest {

    @Mock
    DAOFactory daoFactory;
    @Mock
    CompanyDAO companyDAO;

    @Before
    public void setUp() throws Exception {

        Mockito.when(daoFactory.getCompanyDAO()).thenReturn(companyDAO);
        AbstractDAOUser.setDAOFactory(daoFactory);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCompaniesValid() {
        Page page = Page.createPage(10, 0);
        PageDTO pageDTO =
                PageDTO.builder().withLimit("10").withOffset("0").build();
        Company company1 =
                Company.builder().withID(1).withName("company1").build();
        Company company2 =
                Company.builder().withID(2).withName("company2").build();
        List<Company> companies = new ArrayList<Company>();
        companies.add(company1);
        companies.add(company2);
        try {
            Mockito.when(companyDAO.getPageOfCompanies(page))
                    .thenReturn(companies);
        } catch (SQLException e1) {
            Assert.fail("Absurd");
        }
        ServiceCompany serviceCompany = new ServiceCompanyImpl();
        try {
            Assert.assertEquals(serviceCompany.getCompanies(pageDTO),
                    companies);
        } catch (SQLException e) {
            Assert.fail("SQLException thrown");
        } catch (InvalidPageException e) {
            Assert.fail("InvalidPageException thrown");
        } catch (ProblemListException e) {
            Assert.fail("ProblemListException thrown");
        }
    }

    @Test
    public void testGetCompaniesEmpty() {
        Page page = Page.createPage(10, 10);
        PageDTO pageDTO =
                PageDTO.builder().withLimit("10").withOffset("10").build();
        try {
            Mockito.when(companyDAO.getPageOfCompanies(page))
                    .thenReturn(new ArrayList<Company>());
        } catch (SQLException e1) {
            Assert.fail("Absurd");
        }
        ServiceCompany serviceCompany = new ServiceCompanyImpl();
        try {
            Assert.assertEquals(serviceCompany.getCompanies(pageDTO),
                    new ArrayList<Company>());
        } catch (SQLException e) {
            Assert.fail("SQLException thrown");
        } catch (InvalidPageException e) {
            Assert.fail("InvalidPageException thrown");
        } catch (ProblemListException e) {
            Assert.fail("ProblemListException thrown");
        }
    }

    @Test
    public void testGetCompaniesInvalid1() {
        PageDTO pageDTO =
                PageDTO.builder().withLimit("10").withOffset("-1").build();
        ServiceCompany serviceCompany = new ServiceCompanyImpl();
        try {
            serviceCompany.getCompanies(pageDTO);
            Assert.fail("No exception thrown");
        } catch (SQLException e) {
            Assert.fail("SQLException thrown");
        } catch (InvalidPageException e) {

        } catch (ProblemListException e) {
            Assert.fail("ProblemListException thrown");
        }
    }

    @Test
    public void testGetCompaniesInvalid2() {
        PageDTO pageDTO =
                PageDTO.builder().withLimit("0").withOffset("0").build();
        ServiceCompany serviceCompany = new ServiceCompanyImpl();
        try {
            serviceCompany.getCompanies(pageDTO);
            Assert.fail("No exception thrown");
        } catch (SQLException e) {
            Assert.fail("SQLException thrown");
        } catch (InvalidPageException e) {

        } catch (ProblemListException e) {
            Assert.fail("ProblemListException thrown");
        }
    }

}
