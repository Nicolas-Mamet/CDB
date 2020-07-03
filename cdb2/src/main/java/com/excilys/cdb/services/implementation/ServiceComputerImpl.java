package com.excilys.cdb.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.services.interfaces.ServiceComputer;
import com.excilys.cdb.servlet.Order;

@Service
public class ServiceComputerImpl implements ServiceComputer {

    @Autowired
    ComputerDAO computerDAO;

    @SuppressWarnings("unused")
    private final Logger logger =
            LoggerFactory.getLogger(ServiceComputer.class);

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO)
            throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        logger.debug("getComputers without search nor order");
        return computerDAO.getPageOfComputers(page).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, String search)
            throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        logger.debug("getComputers with search but no order");
        return computerDAO.searchComputers(page, search).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, String search,
            Order order) throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        logger.debug("getComputers with search and order");
        return computerDAO.searchComputers(page, search, order).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, Order order)
            throws DBException, ProblemListException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        logger.debug("getComputers with order but no search");
        return computerDAO.searchComputers(page, order).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());

    }

    @Override
    public Optional<ComputerDTO> getComputer(String string)
            throws NotLongException, DBException {
        long iD = Mapper.mapLong(string);
        return computerDAO.getComputer(iD)
                .flatMap(c -> MapperDTO.computerToDTO(c));
    }

    @Override
    public boolean deleteComputer(String string)
            throws NotLongException, DBException {
        long iD = Mapper.mapLong(string);
        return computerDAO.deleteComputer(iD);
    }

    @Override
    public void createComputer(ComputerDTO computerDTO)
            throws ProblemListException, NullComputerException, DBException {
        Computer computer = MapperDTO.dtoToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        computerDAO.createComputer(computer);
    }

    @Override
    public boolean updateComputer(ComputerDTO computerDTO)
            throws ProblemListException, NullComputerException, DBException {
        Computer computer = MapperDTO.dtoToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        return computerDAO.updateComputer(computer);
    }

    @Override
    public long countComputers() throws DBException {
        return computerDAO.countComputers();
    }

    @Override
    public long countComputers(String search) throws DBException {
        return computerDAO.countComputers(search);
    }

    @Override
    public void deleteComputers(List<String> ids)
            throws ProblemListException, DBException {
        computerDAO.deleteComputers(mapList(ids));
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
