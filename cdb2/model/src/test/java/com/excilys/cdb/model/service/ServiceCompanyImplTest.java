package com.excilys.cdb.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.persistence.CompanyDAO;

public class ServiceCompanyImplTest {

    @Mock
    CompanyDAO companyDAO;

    @InjectMocks
    ServiceCompanyImpl serviceCompany;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCompaniesTest() {
        List<Company> list = new ArrayList<>();
        try {
            Mockito.when(companyDAO.getCompanies()).thenReturn(list);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(list, serviceCompany.getCompanies());
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void getCompaniesPageTest() {
        List<Company> list = new ArrayList<>();
        try {
            Mockito.when(companyDAO.getPageOfCompanies(null))
                    .thenReturn(list);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(list, serviceCompany.getCompanies(null));
        } catch (DBException e) {
            fail("This should not generate an exception");
        }
    }

    @Test
    public void deleteCompanyTest() {
        try {
            serviceCompany.deleteCompany(0);
        } catch (DBException e) {
            fail("This should not generate an exception");
        }
        try {
            Mockito.verify(companyDAO, Mockito.times(1)).deleteCompany(0);
            Mockito.verify(companyDAO, Mockito.times(1))
                    .deleteCompany(Mockito.anyLong());
        } catch (DBException e) {
            fail("Absurd");
        }
    }
}
