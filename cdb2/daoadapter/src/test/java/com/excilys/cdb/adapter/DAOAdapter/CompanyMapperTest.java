package com.excilys.cdb.adapter.DAOAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.excilys.cdb.crossProject.LoggerSetup;
import com.excilys.cdb.crossProject.exceptions.AbsurdException;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Company.CompanyBuilder;
import com.excilys.cdb.model.config.SpringConfigModel;
import com.excilys.cdb.model.config.SpringConfigValidator;
import com.excilys.cdb.model.service.CompanyService;
import com.excilys.cdb.model.service.ComputerService;
import com.excilys.cdb.model.service.ServiceCompanyImpl;
import com.excilys.cdb.model.service.ServiceComputerImpl;
import com.excilys.cdb.model.validator.CompanyValidator;
import com.excilys.cdb.model.validator.ComputerValidator;
import com.excilys.cdb.model.validator.ICompanyValidator;
import com.excilys.cdb.model.validator.IComputerValidator;
import com.excilys.cdb.model.validator.IPageValidator;
import com.excilys.cdb.model.validator.PageValidator;
import com.excilys.cdb.model.validator.Validator;
import com.excilys.cdb.persistence.entity.ECompany;

public class CompanyMapperTest {

    @Configuration
    @ComponentScan(
        basePackageClasses = Company.class,
        excludeFilters = @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                    ComputerService.class, CompanyService.class,
                    Validator.class,
                    ServiceCompanyImpl.class,
                    ServiceComputerImpl.class, SpringConfigModel.class,
                    SpringConfigValidator.class
            }))
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
        public IPageValidator getPageValidator() {
            return Mockito.mock(PageValidator.class);
        }

        @Bean
        public IComputerValidator getComputerValidator() {
            return Mockito.mock(ComputerValidator.class);
        }
    }

    @Mock
    ECompany ecompany;

    @BeforeClass
    public static void setUpClass() {
        LoggerSetup.setDefaultLevelToTrace();
        SpringConfig.init();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapTest() {
        Mockito.when(ecompany.getId()).thenReturn(1L);
        Mockito.when(ecompany.getName()).thenReturn("test");
        validateCompany(true);
        try {
            Company c = new CompanyMapper().map(ecompany);
            assertEquals(1L, c.getId());
            assertEquals("test", c.getName());
        } catch (AbsurdException e) {
            fail("Absurd");
        }
    }

    /**
     * Just to get 100% coverage
     */
    @Test
    public void mapAbsurdTest() {
        Mockito.when(ecompany.getId()).thenReturn(-1L);
        Mockito.when(ecompany.getName()).thenReturn("test");
        validateCompany(false);
        try {
            Company c = new CompanyMapper().map(ecompany);
            fail("Absurd");
        } catch (AbsurdException e) {
        }
    }

    private void validateCompany(boolean valid) {
        try {
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
        } catch (ProblemListException e) {
            fail("Absurd");
        }
    }

}
