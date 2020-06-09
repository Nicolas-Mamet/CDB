package com.excilys.cdb.services.implementation;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.services.interfaces.ServiceComputer;

public class ServiceComputerImpl extends AbstractDAOUser
        implements ServiceComputer {

    @Override
    public List<ComputerDTO> getComputers(PageDTO pageDTO)
            throws SQLException, InvalidPageException, ProblemListException {
        Page page;
        page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        if (page == null || page.getOffset() < 0 || page.getLimit() < 1) {
            throw new InvalidPageException();
        }
        return getDAOFactory().getComputerDAO().getPageOfComputers(page)
                .stream().filter(c -> c != null)
                .map(c -> MapperDTO.ComputerToDTO(c).get())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ComputerDTO> getComputer(String string)
            throws SQLException, NotLongException {
        long iD = Mapper.mapLong(string);
        return getDAOFactory().getComputerDAO().getComputer(iD)
                .flatMap(c -> MapperDTO.ComputerToDTO(c));
    }

    @Override
    public boolean deleteComputer(String string)
            throws SQLException, NotLongException {
        long iD = Mapper.mapLong(string);
        return getDAOFactory().getComputerDAO().deleteComputer(iD);
    }

    @Override
    public void createComputer(ComputerDTO computerDTO)
            throws SQLException, ProblemListException, NullComputerException {
//        Computer computer = buildComputerNoID(name, introduced, discontinued,
//                companyID, companyName);

        Computer computer = MapperDTO.DTOToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        List<Problem> problemList = new ArrayList<Problem>();
        checkCompany(computerDTO.getCompany(), problemList);
        checkCreation(computer, problemList);
        if (problemList.size() > 0) {
            throw new ProblemListException(problemList);
        }
        getDAOFactory().getComputerDAO().createComputer(computer);
    }

    @Override
    public boolean updateComputer(ComputerDTO computerDTO)
            throws SQLException, ProblemListException, NullComputerException {
//        Computer computer = buildComputerWithID(iD, name, introduced,
//                discontinued, companyID, companyName);
        Computer computer = MapperDTO.DTOToComputer(computerDTO)
                .orElseThrow(NullComputerException::new);
        List<Problem> problemList = new ArrayList<Problem>();
        checkCompany(computerDTO.getCompany(), problemList);
        checkUpdate(computer, problemList);
        if (problemList.size() > 0) {
            throw new ProblemListException(problemList);
        }
        return getDAOFactory().getComputerDAO().updateComputer(computer);
    }

    private void checkUpdate(Computer computer, List<Problem> problemList)
            throws SQLException {
        checkID(computer, problemList);
        checkName(computer, problemList);
        checkDates(computer, problemList);

    }

    private void checkCreation(Computer computer, List<Problem> problemList) {
        checkName(computer, problemList);
        checkDates(computer, problemList);
    }

    private void checkID(Computer computer, List<Problem> problemList)
            throws SQLException {
        if (getDAOFactory().getComputerDAO().getComputer(computer.getID())
                .isEmpty()) {
            problemList.add(Problem.createNoComputer(computer.getID() + ""));
        }
    }

    private void checkCompany(CompanyDTO company, List<Problem> problemList)
            throws SQLException {
        if (company != null) {
            Optional<String> name = Optional.empty();
            try {
                name = getServiceFactory().getServiceCompany()
                        .getCompanyName(company.getId());
                Optional<String> name2 = Optional.empty();
                if (name2.isEmpty()) {
                    problemList.add(Problem.createNoCompany(
                            "No company with id =" + company.getId()));
                } else {
                    if (company.getName() != null
                            && company.getName() != name.get()) {
                        problemList.add(Problem.createNoCompany(
                                "Company ID and name not compatible"));
                    }
                }
            } catch (NotLongException e) {
                problemList.add(Problem.createNotALong("CompanyID"));
            }
        }
    }

    private void checkDates(Computer computer, List<Problem> problemList) {
        LocalDateTime introduced = computer.getIntroduced();
        LocalDateTime discontinued = computer.getDiscontinued();
        if (checkDate(introduced, problemList)
                && checkDate(discontinued, problemList)) {
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
            if (date.isBefore(LocalDateTime.parse("1970-01-01T00:00:01"))) {
                problemList.add(Problem.createBefore1970(date.toString()));
            } else {
                return true;
            }
        }
        return false;
    }

    private void checkName(Computer computer, List<Problem> problemList) {
        if (computer.getName() == null) {
            problemList.add(Problem.createNoName());
        }
    }

//    private Computer buildComputerWithID(String iD, String name,
//            String introduced, String discontinued, String companyID,
//            String companyName) throws SQLException, ProblemListException {
//        List<Problem> problemList = new ArrayList<Problem>();
//        ComputerBuilder bob = Computer.builder().withName(name);
//        checkAndBuildID(problemList, bob, iD);
//        checkAndBuildName(problemList, bob, name);
//        checkAndBuildDates(problemList, bob, introduced, discontinued);
//        checkAndBuildCompany(problemList, bob, companyID, companyName);
//        conditionalThrowList(problemList);
//        return bob.build();
//    }
//
//    private Computer buildComputerNoID(String name, String introduced,
//            String discontinued, String companyID, String companyName)
//            throws SQLException, ProblemListException {
//        List<Problem> problemList = new ArrayList<Problem>();
//        ComputerBuilder bob = Computer.builder().withName(name);
//        checkAndBuildName(problemList, bob, name);
//        checkAndBuildDates(problemList, bob, introduced, discontinued);
//        checkAndBuildCompany(problemList, bob, companyID, companyName);
//        conditionalThrowList(problemList);
//        return bob.build();
//    }
//
//    private void checkAndBuildID(List<Problem> problemList, ComputerBuilder bob,
//            String iD) throws SQLException {
//        try {
//            long iDLong = Mapper.mapLong(iD);
//            if (getDAOFactory().getComputerDAO().getComputer(iDLong)
//                    .isPresent()) {
//                bob.withID(iDLong);
//            }
//        } catch (NotLongException e) {
//            problemList.add(Problem.createNotALong(iD));
//        }
//
//    }
//
//    private void checkAndBuildName(List<Problem> list, ComputerBuilder bob,
//            String name) {
//        if (name == null) {
//            list.add(Problem.createNoName());
//        }
//    }
//
//    private void checkAndBuildDates(List<Problem> problemList,
//            ComputerBuilder bob, String introduced, String discontinued) {
//        int count = problemList.size();
//        Optional<LocalDateTime> intro = getDate(problemList, introduced);
//        Optional<LocalDateTime> disco = getDate(problemList, discontinued);
//        if (count == problemList.size()) {
//            checkComputerDates(problemList, intro, disco);
//        }
//        intro.ifPresent(date -> bob.withIntroduced(date));
//        disco.ifPresent(date -> bob.withDiscontinued(date));
//    }
//
//    private Optional<LocalDateTime> getDate(List<Problem> problemList,
//            String date) {
//        try {
//            Optional<LocalDateTime> localDateTime =
//                    Mapper.mapLocalDateTime(date);
//            if (localDateTime.isPresent() && localDateTime.get()
//                    .isBefore(LocalDateTime.parse("1970-01-01T00:00:01"))) {
//                problemList.add(Problem.createBefore1970(date));
//            }
//            return localDateTime;
//        } catch (NotDateException e) {
//            problemList.add(Problem.createNotADate(date));
//            return Optional.empty();
//        }
//    }
//
//    private void checkComputerDates(List<Problem> problemList,
//            Optional<LocalDateTime> introduced,
//            Optional<LocalDateTime> discontinued) {
//        if (introduced.isPresent() && discontinued.isPresent()
//                && introduced.get().isAfter(discontinued.get())) {
//            problemList.add(Problem.createWrongOrder());
//        }
//    }
//
//    private void checkAndBuildCompany(List<Problem> problemList,
//            ComputerBuilder bob, String companyID, String companyName)
//            throws SQLException {
//        try {
//            if (companyID != null && companyName != null) {
//                if (companyID.equals("") && companyName.equals("")) {
//                    return;
//                }
//                Optional<Company> company = getServiceFactory()
//                        .getServiceCompany().getCompany(companyID, companyName);
//                if (company.isPresent()) {
//                    bob.withCompany(company.get());
//                } else {
//                    problemList.add(Problem
//                            .createNoCompany(companyID + " " + companyName));
//                }
//            }
//        } catch (NotLongException e) {
//            problemList.add(Problem.createNotALong(companyID));
//        }
//    }
//
//    private void conditionalThrowList(List<Problem> problemList)
//            throws ProblemListException {
//        if (!problemList.isEmpty()) {
//            throw new ProblemListException(problemList);
//        }
//    }
}
