package com.excilys.cdb.services.implementation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.services.interfaces.ServiceComputer;
import com.excilys.cdb.servlet.Order;

public class ServiceComputerImpl extends AbstractDAOUser
        implements ServiceComputer {

    @SuppressWarnings("unused")
    private final Logger logger =
            LoggerFactory.getLogger(ServiceComputer.class);

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO)
            throws ProblemListException, DBException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        try {
            logger.debug("getComputers without search nor order");
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
            throws ProblemListException, DBException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        try {
            logger.debug("getComputers with search but no order");
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
    public List<ComputerDTO> getComputers(PageDTO pageDTO, String search,
            Order order) throws ProblemListException, DBException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        logger.debug("getComputers with search and order");
        return getDAOFactory().getComputerDAO()
                .searchComputers(page, search, order).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.ComputerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, Order order)
            throws DBException, ProblemListException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        logger.debug("getComputers with order but no search");
        return getDAOFactory().getComputerDAO().searchComputers(page, order)
                .stream().filter(c -> c != null)
                .map(c -> MapperDTO.ComputerToDTO(c).get())
                .collect(Collectors.toList());

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
            long count = getDAOFactory().getComputerDAO().getComputers(search)
                    .size();
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
}
