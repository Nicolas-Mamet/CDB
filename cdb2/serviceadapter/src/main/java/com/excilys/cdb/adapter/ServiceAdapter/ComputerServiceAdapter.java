package com.excilys.cdb.adapter.ServiceAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.Mapper;
import com.excilys.cdb.crossProject.Order;
import com.excilys.cdb.crossProject.exceptions.DBException;
import com.excilys.cdb.crossProject.exceptions.NotLongException;
import com.excilys.cdb.crossProject.exceptions.NullComputerException;
import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.mvc.dto.ComputerDTO;
import com.excilys.cdb.mvc.dto.PageDTO;

@Component
public class ComputerServiceAdapter
        implements com.excilys.cdb.mvc.service.ComputerService {

    @Autowired
    com.excilys.cdb.model.service.ComputerService computerService;

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO)
            throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        return computerService.getComputers(page).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, String search)
            throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        return computerService.getComputers(page, search).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ComputerDTO> getComputer(String id)
            throws NotLongException, DBException {
        long iD = Mapper.mapLong(id);
        return computerService.getComputer(iD)
                .flatMap(c -> MapperDTO.computerToDTO(c));
    }

    @Override
    public boolean deleteComputer(String id)
            throws NotLongException, DBException {
        long iD = Mapper.mapLong(id);
        return computerService.deleteComputer(iD);
    }

    @Override
    public void deleteComputers(List<String> ids)
            throws DBException, ProblemListException {
        computerService.deleteComputers(mapList(ids));
    }

    @Override
    public void createComputer(ComputerDTO computerDTO)
            throws ProblemListException, NullComputerException, DBException {
        Computer computer = MapperDTO.dtoToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        computerService.createComputer(computer);
    }

    @Override
    public boolean updateComputer(ComputerDTO computerDTO)
            throws ProblemListException, NullComputerException, DBException {
        Computer computer = MapperDTO.dtoToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        return computerService.updateComputer(computer);
    }

    @Override
    public long countComputers() throws DBException {
        return computerService.countComputers();
    }

    @Override
    public long countComputers(String search) throws DBException {
        return computerService.countComputers(search);
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, String search,
            Order order) throws ProblemListException, DBException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        return computerService.getComputers(page, search, order).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO, Order order)
            throws DBException, ProblemListException {
        Page page = MapperDTO.dtoToPage(pageDTO).orElse(null);
        return computerService.getComputers(page, order).stream()
                .filter(c -> c != null)
                .map(c -> MapperDTO.computerToDTO(c).get())
                .collect(Collectors.toList());
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
