package com.excilys.cdb.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;

public class ComputerValidatorTest {

    private final static String VALID_INTRODUCED = "1970-01-01T00:00:10";
    private final static String VALID_DISCONTINUED1 = "1970-01-01T00:00:05";
    private final static String VALID_DISCONTINUED2 = "1970-01-01T00:00:15";
    private final static String EARLY = "1969-12-31T23:59:59";
    private final static String LATE = "2038-01-01T00:00:01";

    @Test
    public void validComputerTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateValidIntroduced(bob);
        decorateValidDiscontinued2(bob);
        try {
            new ComputerValidator().validate(bob);
        } catch (ProblemListException e) {
            fail("Failed to validate a valid computer");
        }
    }

    @Test
    public void validComputerNoIntroducedTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateValidDiscontinued2(bob);
        try {
            new ComputerValidator().validate(bob);
        } catch (ProblemListException e) {
            fail("Failed to validate a valid computer");
        }
    }

    @Test
    public void emptyNameTest() {
        ComputerBuilder bob = Computer.builder();
        decorateEmptyName(bob);
        decorateValidIntroduced(bob);
        decorateValidDiscontinued2(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.NONAME);
        }
    }

    @Test
    public void nullNameTest() {
        ComputerBuilder bob = Computer.builder();
        decorateNullName(bob);
        decorateValidIntroduced(bob);
        decorateValidDiscontinued2(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.NONAME);
        }
    }

    @Test
    public void earlyIntroducedTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateEarlyIntroduced(bob);
        decorateValidDiscontinued2(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.BEFORE1970, EARLY);
        }
    }

    @Test
    public void lateIntroducedTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateLateIntroduced(bob);
        decorateValidDiscontinued2(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.BEFORE1970, LATE);
        }
    }

    @Test
    public void earlyDiscontinuedTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateValidIntroduced(bob);
        decorateEarlyDiscontinued(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.BEFORE1970, EARLY);
        }
    }

    @Test
    public void lateDiscontinuedTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateValidIntroduced(bob);
        decorateLateDiscontinued(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.BEFORE1970, LATE);
        }
    }

    @Test
    public void wrongOrderTest() {
        ComputerBuilder bob = Computer.builder();
        decorateValidName(bob);
        decorateValidIntroduced(bob);
        decorateValidDiscontinued1(bob);
        try {
            new ComputerValidator().validate(bob);
            fail("Suceeded in validating an invalid computer");
        } catch (ProblemListException e) {
            assertSize(e.getList(), 1);
            assertContains(e.getList(), 0, Problem.Nature.WRONGORDER);
        }
    }

    private ComputerBuilder decorateValidName(ComputerBuilder bob) {
        return bob.withName("Johny");
    }

    private ComputerBuilder decorateEmptyName(ComputerBuilder bob) {
        return bob.withName("");
    }

    private ComputerBuilder decorateNullName(ComputerBuilder bob) {
        return bob.withName(null);
    }

    private ComputerBuilder decorateValidIntroduced(ComputerBuilder bob) {
        return bob.withIntroduced(LocalDateTime.parse(VALID_INTRODUCED));
    }

    private ComputerBuilder decorateEarlyIntroduced(ComputerBuilder bob) {
        return bob.withIntroduced(LocalDateTime.parse(EARLY));
    }

    private ComputerBuilder decorateLateIntroduced(ComputerBuilder bob) {
        return bob.withIntroduced(LocalDateTime.parse(LATE));
    }

    private ComputerBuilder decorateValidDiscontinued1(ComputerBuilder bob) {
        return bob.withDiscontinued(LocalDateTime.parse(VALID_DISCONTINUED1));
    }

    private ComputerBuilder decorateValidDiscontinued2(ComputerBuilder bob) {
        return bob.withDiscontinued(LocalDateTime.parse(VALID_DISCONTINUED2));
    }

    private ComputerBuilder decorateEarlyDiscontinued(ComputerBuilder bob) {
        return bob.withDiscontinued(LocalDateTime.parse(EARLY));
    }

    private ComputerBuilder decorateLateDiscontinued(ComputerBuilder bob) {
        return bob.withDiscontinued(LocalDateTime.parse(LATE));
    }

    private void assertSize(List<?> list, int size) {
        assertEquals(size, list.size());
    }

    private void assertContains(
            List<Problem> list,
            int index,
            Problem.Nature nature,
            String origin) {
        assertEquals(list.get(index).getNature(), nature);
        // System.out.println(list.get(index).getOrigin().get());
        assertEquals(list.get(index).getOrigin().get(), origin);
    }

    private void assertContains(
            List<Problem> list,
            int index,
            Problem.Nature nature) {
        assertEquals(list.get(index).getNature(), nature);
    }

}
