package com.excilys.cdb.adapter.ServiceAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.crossProject.Mapper;
import com.excilys.cdb.crossProject.exceptions.AbsurdOptionalException;
import com.excilys.cdb.crossProject.exceptions.NotDateException;
import com.excilys.cdb.crossProject.exceptions.NotLongException;
import com.excilys.cdb.crossProject.exceptions.Problem;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.mvc.dto.CompanyDTO;
import com.excilys.cdb.mvc.dto.ComputerDTO;
import com.excilys.cdb.mvc.dto.PageDTO;

public class MapperDTO {
    public static Optional<ComputerDTO> computerToDTO(Computer computer) {
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
                            companyToDTO(computer.getCompany()).orElse(null))
                    .build());
        }
    }

    public static Optional<CompanyDTO> companyToDTO(Company company) {
        if (company == null) {
            return Optional.empty();
        } else {
            return Optional.of(CompanyDTO.builder().withName(company.getName())
                    .withId(company.getId() + "").build());
        }

    }

    public static Optional<Computer> dtoToComputer(ComputerDTO computer)
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

    public static Optional<Company> dtoToCompany(CompanyDTO company)
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

    public static Optional<Page> dtoToPage(PageDTO page)
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

    private static Optional<LocalDateTime> mapLocalDateTime(
            String introduced,
            List<Problem> problemList) {
        try {
            return mapLocalDateTime(introduced);
        } catch (NotDateException e) {
            problemList.add(Problem.createNotADate(introduced));
            return Optional.empty();
        }
    }

    private static Optional<Company> mapCompany(
            CompanyDTO companyDTO,
            List<Problem> problemList) {
        Optional<Company> company = Optional.empty();
        try {
            company = MapperDTO.dtoToCompany(companyDTO);
        } catch (ProblemListException e) {
            problemList.addAll(e.getList());
        }
        return company;
    }

    private static Optional<Long> mapLong(
            String longString,
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

    /**
     *
     * @param string
     * @return
     * @throws NotDateException
     */
    private static Optional<LocalDateTime> mapLocalDateTime(String string)
            throws NotDateException {
        if (string == null || string.equals("")) {
            return Optional.empty();
        }
        LocalDateTime date;
        try {
            date = LocalDate.parse(string).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new NotDateException(string);
        }
        return Optional.of(date);
    }
}