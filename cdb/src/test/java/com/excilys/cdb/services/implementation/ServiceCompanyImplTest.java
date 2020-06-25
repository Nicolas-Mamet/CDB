package com.excilys.cdb.services.implementation;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

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

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.Page.PageBuilder;
import com.excilys.cdb.model.validator.CompanyValidator;
import com.excilys.cdb.model.validator.PageValidator;
import com.excilys.cdb.persistence.implementation.DAOFactory;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.services.interfaces.ServiceCompany;

@RunWith(MockitoJUnitRunner.class)
public class ServiceCompanyImplTest {

    @Mock
    DAOFactory daoFactory;
    @Mock
    CompanyDAO companyDAO;
    @Mock
    CompanyValidator companyValidator;
    @Mock
    PageValidator pageValidator;

    @Before
    public void setUp() throws Exception {

        Mockito.when(daoFactory.getCompanyDAO()).thenReturn(companyDAO);
        AbstractDAOUser.setDAOFactory(daoFactory);
        PageBuilder.setValidator(pageValidator);
        CompanyBuilder.setValidator(companyValidator);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCompaniesValid() {
        Page page;
        PageDTO pageDTO;
        Company company1, company2;
        CompanyDTO companyDTO1, companyDTO2;
        try {
            page = preparePage(0, 10, true);
            pageDTO = PageDTO.builder().withLimit("10").withOffset("0").build();
            company1 = prepareCompany(1L, "company1", true);
            company2 = prepareCompany(2L, "company2", true);
            companyDTO1 = CompanyDTO.builder().withId("1").withName("company1")
                    .build();
            companyDTO2 = CompanyDTO.builder().withId("2").withName("company2")
                    .build();
        } catch (ProblemListException e) {
            fail("All companies/pages are correct so this should not happen");
            return;
        }
        List<Company> companies = new ArrayList<>();
        List<CompanyDTO> companiesDTO = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        companiesDTO.add(companyDTO1);
        companiesDTO.add(companyDTO2);
        try {
            Mockito.when(companyDAO.getPageOfCompanies(page))
                    .thenReturn(companies);
        } catch (SQLException e1) {
            Assert.fail("Absurd");
        }
        ServiceCompany serviceCompany = new ServiceCompanyImpl();
        try {

            Assert.assertEquals(serviceCompany.getCompanies(pageDTO),
                    companiesDTO);
        } catch (DBException e) {
            Assert.fail("DBException thrown");
        } catch (ProblemListException e) {
            Assert.fail("ProblemListException thrown");
        }
    }

    @Test
    public void testGetCompaniesEmpty() {
        Page page;
        try {
            page = Page.builder().withLimit(10).withOffset(10).build();
        } catch (ProblemListException e2) {
            fail("The page should be correct so this should not happen");
            return;
        }
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
        } catch (DBException e) {
            Assert.fail("DBException thrown");
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
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(pageValidator).validate(any(PageBuilder.class));
            serviceCompany.getCompanies(pageDTO);
            Assert.fail("No exception thrown");
        } catch (DBException e) {
            Assert.fail("DBException thrown");
        } catch (ProblemListException e) {
        }
    }

    @Test
    public void testGetCompaniesInvalid2() {
        PageDTO pageDTO =
                PageDTO.builder().withLimit("0").withOffset("0").build();
        ServiceCompany serviceCompany = new ServiceCompanyImpl();
        try {
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(pageValidator).validate(any(PageBuilder.class));
            serviceCompany.getCompanies(pageDTO);
            Assert.fail("No exception thrown");
        } catch (DBException e) {
            Assert.fail("DBException thrown");
        } catch (ProblemListException e) {
        }
    }

    private Page preparePage(long offset, long limit, boolean valid)
            throws ProblemListException {
        PageBuilder bob = Page.builder();
        if (valid) {
            Mockito.doNothing().when(pageValidator).validate(bob);
        } else {
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(pageValidator).validate(bob);
        }
        return bob.withOffset(offset).withLimit(limit).build();
    }

    private Company prepareCompany(long id, String name, boolean valid)
            throws ProblemListException {
        CompanyBuilder bob = Company.builder();
        if (valid) {
            Mockito.doNothing().when(companyValidator).validate(bob);
        } else {
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(companyValidator).validate(bob);
        }
        return bob.withID(id).withName(name).build();
    }
}
