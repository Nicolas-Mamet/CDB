package com.excilys.cdb.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

public class MapperDTO {
    public static Optional<ComputerDTO> ComputerToDTO(Computer computer) {
        if (computer == null) {
            return Optional.empty();
        } else {
            return Optional.of(ComputerDTO.builder()
                    .withId(computer.getId() + "").withName(computer.getName())
                    .withDiscontinued(Optional
                            .ofNullable(computer.getDiscontinued())
                            .map(d -> d.toLocalDate().toString()).orElse(null))
                    .withIntroduced(Optional
                            .ofNullable(computer.getIntroduced())
                            .map(d -> d.toLocalDate().toString()).orElse(null))
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
            Optional<Long> iD = mapLong(computer.getId(), problemList);
            Optional<LocalDateTime> introduced =
                    mapLocalDateTime(computer.getIntroduced(), problemList);
            Optional<LocalDateTime> discontinued =
                    mapLocalDateTime(computer.getDiscontinued(), problemList);
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
            throws ProblemListException {
        if (company == null) {
            return Optional.empty();
        } else {
            long iD;
            try {
                iD = Mapper.mapLong(company.getId());
            } catch (NotLongException e) {
                throw new ProblemListException(
                        Arrays.asList(Problem.createNotALong(company.getId())));
            }
            if (iD == 0) {
                return Optional.empty();
            } else {
                return Optional.of(Company.builder().withID(iD)
                        .withName(company.getName()).build());
            }
        }
    }

    public static Optional<Page> DTOToPage(PageDTO page)
            throws ProblemListException {
        if (page == null) {
            return Optional.empty();
        } else {
            List<Problem> problemList = new ArrayList<Problem>();
            Optional<Long> limit = mapLong(page.getLimit(), problemList);
            Optional<Long> offset = mapLong(page.getOffset(), problemList);
            if (problemList.size() > 0) {
                throw new ProblemListException(problemList);
            } else {
                return Optional.of(Page.builder()
                        .withLimit(
                                limit.orElseThrow(AbsurdOptionalException::new))
                        .withOffset(offset
                                .orElseThrow(AbsurdOptionalException::new))
                        .build());
            }
        }
    }

    private static Optional<LocalDateTime> mapLocalDateTime(String introduced,
            List<Problem> problemList) {
        try {
            return Mapper.mapLocalDateTime(introduced);
        } catch (NotDateException e) {
            problemList.add(Problem.createNotADate(introduced));
            return Optional.empty();
        }
    }

    private static Optional<Company> mapCompany(CompanyDTO companyDTO,
            List<Problem> problemList) {
        Optional<Company> company = Optional.empty();
        try {
            company = MapperDTO.DTOToCompany(companyDTO);
        } catch (ProblemListException e) {
            problemList.addAll(e.getList());
        }
        return company;
    }

    private static Optional<Long> mapLong(String longString,
            List<Problem> problemList) {
        if (longString == null) {
            return Optional.of(-1L);
        }
        try {
            return Optional.of(Mapper.mapLong(longString));
        } catch (NotLongException e) {
            problemList.add(Problem.createNotALong(longString));
            return Optional.empty();
        }
    }
}
