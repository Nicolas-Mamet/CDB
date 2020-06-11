package com.excilys.cdb.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.AbsurdOptionalException;
import com.excilys.cdb.exceptions.NotDateException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.util.GeneralUtil;

public class MapperDTO {
    public static Optional<ComputerDTO> ComputerToDTO(Computer computer) {
        if (computer == null) {
            return Optional.empty();
        } else {
            return Optional.of(ComputerDTO.builder()
                    .withID(computer.getID() + "").withName(computer.getName())
                    .withDiscontinued(GeneralUtil
                            .toString(computer.getDiscontinued()).orElse(null))
                    .withIntroduced(GeneralUtil
                            .toString(computer.getIntroduced()).orElse(null))
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
            Optional<Long> iD =
                    mapLong(computer.getID(), problemList, "ComputerID");
            Optional<LocalDateTime> introduced = mapLocalDateTime(
                    computer.getIntroduced(), problemList, "introduced");
            Optional<LocalDateTime> discontinued = mapLocalDateTime(
                    computer.getDiscontinued(), problemList, "discontinued");
            Optional<Company> company =
                    mapCompany(computer.getCompany(), problemList);
            if (problemList.size() > 0) {
                throw new ProblemListException(problemList);
            } else {
                return Optional.of(Computer.builder()
                        .withID(iD.orElseThrow(AbsurdOptionalException::new))
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
            List<Problem> problemList = new ArrayList<Problem>();
            Optional<Long> limit =
                    mapLong(page.getLimit(), problemList, "limit");
            Optional<Long> offset =
                    mapLong(page.getOffset(), problemList, "offset");
            if (problemList.size() > 0) {
                throw new ProblemListException(problemList);
            } else {
                return Optional.of(Page.createPage(
                        limit.orElseThrow(AbsurdOptionalException::new),
                        offset.orElseThrow(AbsurdOptionalException::new)));
            }
        }
    }

    private static Optional<LocalDateTime> mapLocalDateTime(String introduced,
            List<Problem> problemList, String message) {
        try {
            return Mapper.mapLocalDateTime(introduced);
        } catch (NotDateException e) {
            problemList.add(Problem.createNotADate(message));
            return Optional.empty();
        }
    }

    private static Optional<Company> mapCompany(CompanyDTO companyDTO,
            List<Problem> problemList) {
        Optional<Company> company = Optional.empty();
        try {
            company = MapperDTO.DTOToCompany(companyDTO);
        } catch (NotLongException e) {
            problemList.add(Problem.createNotALong("CompanyID"));
        }
        return company;
    }

    private static Optional<Long> mapLong(String longString,
            List<Problem> problemList, String message) {
        try {
            return Optional.of(Mapper.mapLong(longString));
        } catch (NotLongException e) {
            problemList.add(Problem.createNotALong(message));
            return Optional.empty();
        }
    }

}
