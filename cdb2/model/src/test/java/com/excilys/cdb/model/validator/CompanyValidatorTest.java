package com.excilys.cdb.model.validator;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;

public class CompanyValidatorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValid() {
        try {
            new CompanyValidator().validate(Company.builder().withID(10));
        } catch (ProblemListException e) {
            fail("Failed to validate a valid company");
        }
    }

    @Test
    public void testInvalid() {
        try {
            new CompanyValidator().validate(Company.builder().withID(0));
            fail("succeeded to validate an invalid company");
        } catch (ProblemListException e) {
            assert (e.getList().size() == 1);
            assert (e.getList().get(0).getNature() == Problem.Nature.NOCOMPANY);
        }
    }

}
