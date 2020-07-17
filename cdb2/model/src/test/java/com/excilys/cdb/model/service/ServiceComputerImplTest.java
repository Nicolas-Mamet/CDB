package com.excilys.cdb.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.persistence.ComputerDAO;

public class ServiceComputerImplTest {

    @Mock
    ComputerDAO computerDAO;

    @InjectMocks
    ServiceComputerImpl serviceComputer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getComputersTest() {
        List<Computer> list = new ArrayList<>();
        try {
            Mockito.when(computerDAO.getPageOfComputers(null)).thenReturn(list);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(list, serviceComputer.getComputers(null));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void getComputersSearchTest() {
        List<Computer> list = new ArrayList<>();
        try {
            Mockito.when(
                    computerDAO.searchComputers((Page) null, (String) null))
                    .thenReturn(list);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(list,
                    serviceComputer.getComputers((Page) null, (String) null));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void getComputersSearchOrderTest() {
        List<Computer> list = new ArrayList<>();
        try {
            Mockito.when(
                    computerDAO.searchComputers((Page) null, (String) null,
                            (Order) null))
                    .thenReturn(list);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(list, serviceComputer.getComputers((Page) null,
                    (String) null, (Order) null));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void getComputersOrderTest() {
        List<Computer> list = new ArrayList<>();
        try {
            Mockito.when(
                    computerDAO.searchComputers((Page) null,
                            (Order) null))
                    .thenReturn(list);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(list,
                    serviceComputer.getComputers((Page) null, (Order) null));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void getComputerTest() {
        Optional<Computer> computer = Optional.empty();
        try {
            Mockito.when(
                    computerDAO.getComputer(0))
                    .thenReturn(computer);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(computer,
                    serviceComputer.getComputer(0));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void deleteComputerTest() {
        Optional<Computer> computer = Optional.empty();
        try {
            Mockito.when(
                    computerDAO.deleteComputer(0))
                    .thenReturn(true);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assert (serviceComputer.deleteComputer(0));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void createComputerTest() {
        try {
            serviceComputer.createComputer(null);
        } catch (DBException e) {
            fail("This should not generate an exception");
        }
        try {
            Mockito.verify(computerDAO, Mockito.times(1)).createComputer(null);
            Mockito.verify(computerDAO, Mockito.times(1))
                    .createComputer(Mockito.any());
        } catch (DBException e) {
            fail("Absurd");
        }
    }

    @Test
    public void updateComputerTest() {
        try {
            Mockito.when(
                    computerDAO.updateComputer(null))
                    .thenReturn(true);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assert (serviceComputer.updateComputer(null));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void countComputerTest() {
        try {
            Mockito.when(
                    computerDAO.countComputers())
                    .thenReturn(0L);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(0L, serviceComputer.countComputers());
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void countComputerSearchTest() {
        try {
            Mockito.when(
                    computerDAO.countComputers(null))
                    .thenReturn(0L);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            assertEquals(0L, serviceComputer.countComputers(null));
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }

    @Test
    public void deleteComputersTest() {
        try {
            serviceComputer.deleteComputers(null);
        } catch (DBException e) {
            fail("Absurd");
        }
        try {
            Mockito.verify(computerDAO, Mockito.times(1)).deleteComputers(null);
            Mockito.verify(computerDAO, Mockito.times(1))
                    .deleteComputers(Mockito.any());
        } catch (DBException e) {
            fail("This should not generate an Exception");
        }
    }
}
