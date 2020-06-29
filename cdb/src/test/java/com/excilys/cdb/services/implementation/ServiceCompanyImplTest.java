package com.excilys.cdb.services.implementation;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.logger.LoggerSetup;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.Page.PageBuilder;
import com.excilys.cdb.model.validator.CompanyValidator;
import com.excilys.cdb.model.validator.ComputerValidator;
import com.excilys.cdb.model.validator.ICompanyValidator;
import com.excilys.cdb.model.validator.IComputerValidator;
import com.excilys.cdb.model.validator.PageValidator;
import com.excilys.cdb.model.validator.Validator;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;

public class ServiceCompanyImplTest {

    @Configuration
    @ComponentScan(basePackageClasses = Company.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
            Validator.class }))
    public static class SpringConfig {

        private static final void init() {
        }

        private static final AnnotationConfigApplicationContext APP_CONTEXT =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        @Bean
        public ICompanyValidator getCompanyValidator() {
            return Mockito.mock(CompanyValidator.class);
        }

        @Bean
        public Validator<PageBuilder> getPageValidator() {
            return Mockito.mock(PageValidator.class);
        }

        @Bean
        public IComputerValidator getComputerValidator() {
            return Mockito.mock(ComputerValidator.class);
        }
    }

    @Mock
    CompanyDAO companyDAO;

    @InjectMocks
    public ServiceCompanyImpl serviceCompany;

    static {
        LoggerSetup.setDefaultLevelToTrace();
    }

    static {
        SpringConfig.init();
    }
    SpringConfig config = SpringConfig.APP_CONTEXT.getBean(SpringConfig.class);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        MockitoAnnotations.initMocks(config);
//        assertNotNull(config.companyValidator);
//        assertNotNull(config.pageValidator);
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
            fail("ABSURD All companies/pages are correct so this should not happen");
            return;
        }
        List<Company> companies = new ArrayList<>();
        List<CompanyDTO> companiesDTO = new ArrayList<>();
        companies.add(company1);
        companies.add(company2);
        companiesDTO.add(companyDTO1);
        companiesDTO.add(companyDTO2);
        try {
            validatePage(true);
        } catch (ProblemListException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        try {
            Mockito.when(companyDAO.getPageOfCompanies(page))
                    .thenReturn(companies);
        } catch (DBException e1) {
            Assert.fail("Absurd");
        }
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
            page = preparePage(10, 10, true);
            // page = Page.builder().withLimit(10).withOffset(10).build();
        } catch (ProblemListException e2) {
            fail("ABSURD The page should be correct so this should not happen");
            return;
        }
        PageDTO pageDTO =
                PageDTO.builder().withLimit("10").withOffset("10").build();
        try {
            validatePage(true);
        } catch (ProblemListException e2) {
            fail("Absurd)");
        }
        try {
            Mockito.when(companyDAO.getPageOfCompanies(page))
                    .thenReturn(new ArrayList<Company>());

        } catch (DBException e1) {
            Assert.fail("Absurd");
        }
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
        try {

            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(SpringConfig.APP_CONTEXT.getBean(PageValidator.class))
                    .validate(any(PageBuilder.class));
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
        try {
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(SpringConfig.APP_CONTEXT.getBean(PageValidator.class))
                    .validate(any(PageBuilder.class));
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
        validatePage(valid);
        return bob.withOffset(offset).withLimit(limit).build();
    }

    private Company prepareCompany(long id, String name, boolean valid)
            throws ProblemListException {
        CompanyBuilder bob = Company.builder();
        validateCompany(valid);
        return bob.withID(id).withName(name).build();
    }

    private void validatePage(boolean valid) throws ProblemListException {
        if (valid) {
            Mockito.doNothing()
                    .when(SpringConfig.APP_CONTEXT.getBean(PageValidator.class))
                    .validate(any(PageBuilder.class));
        } else {
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(SpringConfig.APP_CONTEXT.getBean(PageValidator.class))
                    .validate(any(PageBuilder.class));
        }
    }

    private void validateCompany(boolean valid) throws ProblemListException {
        if (valid) {
            Mockito.doNothing()
                    .when(SpringConfig.APP_CONTEXT
                            .getBean(CompanyValidator.class))
                    .validate(any(CompanyBuilder.class));
        } else {
            Mockito.doThrow(new ProblemListException(new ArrayList<>()))
                    .when(SpringConfig.APP_CONTEXT
                            .getBean(CompanyValidator.class))
                    .validate(any(CompanyBuilder.class));
        }
    }
}
