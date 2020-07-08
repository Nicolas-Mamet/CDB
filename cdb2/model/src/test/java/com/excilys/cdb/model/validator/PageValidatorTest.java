package com.excilys.cdb.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Page;

public class PageValidatorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValid() {
        try {
            new PageValidator()
                    .validate(Page.builder().withLimit(10).withOffset(10));
        } catch (ProblemListException e) {
            fail("Failed to validate a valid page");
        }
    }

    @Test
    public void testInvalidLimit() {
        try {
            new PageValidator()
                    .validate(Page.builder().withLimit(0).withOffset(10));
            fail("Succeeded to validate an invalid page");
        } catch (ProblemListException e) {
            assert (e.getList().size() == 1);
            assert (e.getList().get(0)
                    .getNature() == Problem.Nature.WRONGLIMIT);
        }
    }

    @Test
    public void testInvalidOffset() {
        try {
            new PageValidator()
                    .validate(Page.builder().withLimit(10).withOffset(-1));
            fail("Succeeded to validate an invalid page");
        } catch (ProblemListException e) {
            assert (e.getList().size() == 1);
            assert (e.getList().get(0)
                    .getNature() == Problem.Nature.WRONGOFFSET);
        }
    }

    @Test
    public void testInvalidBoth() {
        try {
            new PageValidator()
                    .validate(Page.builder().withLimit(0).withOffset(-1));
            fail("Succeeded to validate an invalid page");
        } catch (ProblemListException e) {
            assertEquals(2, e.getList().size());
            assert (e.getList().get(0)
                    .getNature() == Problem.Nature.WRONGOFFSET);
            assert (e.getList().get(1)
                    .getNature() == Problem.Nature.WRONGLIMIT);
        }
    }

}
