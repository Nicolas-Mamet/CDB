package com.excilys.cdb.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.NotDateException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

import util.GeneralUtil;

public class MapperDTO {
    public static Optional<ComputerDTO> ComputerToDTO(Computer computer) {
        if (computer == null) {
            return Optional.empty();
        } else {
            return Optional.of(ComputerDTO.builder()
                    .withID(computer.getID() + "").withName(computer.getName())
                    .withDiscontinued(
                            GeneralUtil.toString(computer.getDiscontinued()))
                    .withIntroduced(
                            GeneralUtil.toString(computer.getIntroduced()))
                    .withCompany(
                            CompanyToDTO(computer.getCompany()).orElse(null))
                    .build());
        }
    }

    public static Optional<CompanyDTO> CompanyToDTO(Company company) {
        if (company == null) {
            return Optional.empty();
        } else {
            return Optional.of(CompanyDTO.builder().withName(company.getName())
                    .withId(company.getId() + "").build());
        }

    }

    public static Optional<Computer> DTOToComputer(ComputerDTO computer)
            throws ProblemListException {
        if (computer == null) {
            return Optional.empty();
        } else {
            List<Problem> problemList = new ArrayList<Problem>();
            Long iD = 0L;
            try {
                iD = Mapper.mapLong(computer.getID());
            } catch (NotLongException e) {
                problemList.add(Problem.createNotALong("ComputerID"));
            }
            Optional<LocalDateTime> introduced = Optional.empty();
            try {
                introduced = Mapper.mapLocalDateTime(computer.getIntroduced());
            } catch (NotDateException e) {
                problemList.add(Problem.createNotADate("Introduced"));
            }
            Optional<LocalDateTime> discontinued = Optional.empty();
            try {
                discontinued =
                        Mapper.mapLocalDateTime(computer.getDiscontinued());
            } catch (NotDateException e) {
                problemList.add(Problem.createNotADate("Discontinued"));
            }
            Optional<Company> company = Optional.empty();
            try {
                company = MapperDTO.DTOToCompany(computer.getCompany());
            } catch (NotLongException e) {
                problemList.add(Problem.createNotALong("CompanyID"));
            }
            if (problemList.size() > 0) {
                throw new ProblemListException(problemList);
            } else {
                return Optional.of(Computer.builder().withID(iD)
                        .withName(computer.getName())
                        .withIntroduced(introduced.orElse(null))
                        .withDiscontinued(discontinued.orElse(null))
                        .withCompany(company.orElse(null)).build());
            }
        }
    }

    public static Optional<Company> DTOToCompany(CompanyDTO company)
            throws NotLongException {
        if (company == null) {
            return Optional.empty();
        } else {
            Long iD = Mapper.mapLong(company.getId());
            return Optional.of(Company.builder().withID(iD)
                    .withName(company.getName()).build());
        }
    }

    public static Optional<Page> DTOToPage(PageDTO page)
            throws ProblemListException {
        if (page == null) {
            return Optional.empty();
        } else {
            Long offset = null;
            Long limit = null;
            List<Problem> problemList = new ArrayList<Problem>();
            try {
                limit = Mapper.mapLong(page.getLimit());
            } catch (NotLongException e) {
                problemList.add(Problem.createNotALong("PageLimit"));
            }
            try {
                offset = Mapper.mapLong(page.getOffset());
            } catch (NotLongException e) {
                problemList.add(Problem.createNotALong("PageOffset"));
            }
            if (problemList.size() > 0) {
                throw new ProblemListException(problemList);
            } else {
                return Optional.of(Page.createPage(limit, offset));
            }
        }
    }
}
