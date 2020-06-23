package com.excilys.cdb.services.implementation;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.services.interfaces.ServiceComputer;

public class ServiceComputerImpl extends AbstractDAOUser
        implements ServiceComputer {

    private final Logger logger =
            LoggerFactory.getLogger(ServiceComputer.class);

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO)
            throws InvalidPageException, ProblemListException, DBException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        if (page == null || page.getOffset() < 0 || page.getLimit() < 1) {
            throw new InvalidPageException();
        }
        try {
            return getDAOFactory().getComputerDAO().getPageOfComputers(page)
                    .stream().filter(c -> c != null)
                    .map(c -> MapperDTO.ComputerToDTO(c).get())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, String search)
            throws ProblemListException, InvalidPageException, DBException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        if (page == null || page.getOffset() < 0 || page.getLimit() < 1) {
            throw new InvalidPageException();
        }
        try {
            return getDAOFactory().getComputerDAO()
                    .searchComputers(page, search).stream()
                    .filter(c -> c != null)
                    .map(c -> MapperDTO.ComputerToDTO(c).get())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<ComputerDTO> getComputer(String string)
            throws NotLongException, DBException {
        long iD = Mapper.mapLong(string);
        try {
            return getDAOFactory().getComputerDAO().getComputer(iD)
                    .flatMap(c -> MapperDTO.ComputerToDTO(c));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean deleteComputer(String string)
            throws NotLongException, DBException {
        long iD = Mapper.mapLong(string);
        try {
            return getDAOFactory().getComputerDAO().deleteComputer(iD);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void createComputer(ComputerDTO computerDTO)
            throws ProblemListException, NullComputerException, DBException {
        Computer computer = MapperDTO.DTOToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        List<Problem> problemList = new ArrayList<Problem>();
        checkCompany(computer.getCompany(), problemList);
        checkCreation(computer, problemList);
        if (problemList.size() > 0) {
            throw new ProblemListException(problemList);
        }
        try {
            getDAOFactory().getComputerDAO().createComputer(computer);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean updateComputer(ComputerDTO computerDTO)
            throws ProblemListException, NullComputerException, DBException {
        Computer computer = MapperDTO.DTOToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        logger.debug(computer.toString());
        List<Problem> problemList = new ArrayList<Problem>();
        checkCompany(computer.getCompany(), problemList);
        checkUpdate(computer, problemList);
        if (problemList.size() > 0) {
            throw new ProblemListException(problemList);
        }
        try {
            return getDAOFactory().getComputerDAO().updateComputer(computer);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public long countComputers() throws DBException {
        try {
            return getDAOFactory().getComputerDAO().getComputers().size();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public long countComputers(String search) throws DBException {
        try {
            long count;
            count = getDAOFactory().getComputerDAO().getComputers(search)
                    .size();
            logger.debug(Long.toString(count));
            return count;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deleteComputers(List<String> ids)
            throws ProblemListException, DBException {
        try {
            getDAOFactory().getComputerDAO().deleteComputers(mapList(ids));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private List<Long> mapList(List<String> ids) throws ProblemListException {
        List<Problem> problemList = new ArrayList<>();
        List<Long> idsLong = new ArrayList<>();
        for (String id : ids) {
            try {
                idsLong.add(Mapper.mapLong(id));
            } catch (NotLongException e) {
                problemList.add(Problem.createNotALong(id));
            }
        }
        if (problemList.size() > 0) {
            throw new ProblemListException(problemList);
        }
        return idsLong;
    }

    private void checkUpdate(Computer computer, List<Problem> problemList)
            throws DBException {
        checkName(computer, problemList);
        checkDates(computer, problemList);
        checkID(computer, problemList);
    }

    private void checkCreation(Computer computer, List<Problem> problemList) {
        checkName(computer, problemList);
        checkDates(computer, problemList);
    }

    private void checkID(Computer computer, List<Problem> problemList)
            throws DBException {
        try {
            if (getDAOFactory().getComputerDAO().getComputer(computer.getID())
                    .isEmpty()) {
                problemList
                        .add(Problem.createNoComputer(computer.getID() + ""));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private void checkCompany(Company company, List<Problem> problemList)
            throws DBException {
        if (company != null) {
            Optional<String> name = Optional.empty();
            name = getServiceFactory().getServiceCompany()
                    .getCompanyName(company.getId());
            if (company.getName() != null && company.getName() != name.get()) {
                problemList.add(Problem
                        .createNoCompany("Company ID and name not compatible"));
            }
        }
    }

    private void checkDates(Computer computer, List<Problem> problemList) {
        LocalDateTime introduced = computer.getIntroduced();
        LocalDateTime discontinued = computer.getDiscontinued();
        if (checkDate(introduced, problemList)
                & checkDate(discontinued, problemList)) {
            checkAnteriority(introduced, discontinued, problemList);
        }
    }

    private void checkAnteriority(LocalDateTime introduced,
            LocalDateTime discontinued, List<Problem> problemList) {
        if (introduced.isAfter(discontinued)) {
            problemList.add(Problem.createWrongOrder());
        }
    }

    private boolean checkDate(LocalDateTime date, List<Problem> problemList) {
        if (date != null) {
            if (date.isBefore(LocalDateTime.parse("1970-01-01T00:00:01"))
                    || date.isAfter(
                            LocalDateTime.parse("2038-01-01T00:00:00"))) {
                problemList.add(Problem.createBefore1970(date.toString()));
            }
            return true;
        }
        return false;
    }

    private void checkName(Computer computer, List<Problem> problemList) {
        if (computer.getName() == null
                || computer.getName().contentEquals("")) {
            problemList.add(Problem.createNoName());
        }
    }
}
