package com.excilys.cdb.cli;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.exceptions.InvalidPageException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.NullComputerException;
import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;

public class ServiceCaller extends AbstractServiceUser {

    private static ReadFromCL reader;

    public static void setReader(ReadFromCL reader) {
        ServiceCaller.reader = reader;
    }

    public static void printComputerList() throws SQLException, IOException {
        PageManager pageManager = null;
        pageManager = PageManager.createPageManager(readLimit());
        boolean tryAgain = true;
        while (tryAgain) {
            try {
                List<ComputerDTO> computers = null;
                try {
                    computers = getServiceFactory().getServiceComputer()
                            .getComputers(pageManager.next());
                } catch (ProblemListException e) {
                    System.out.println("Absurd (page manager is bugged?)");
                    e.printStackTrace();
                    System.exit(1);
                }
                tryAgain = computers.size() != 0;
                computers.stream()
                        .forEach(company -> System.out.println(company));
            } catch (InvalidPageException e) {
                System.out.println("Limit must be positive");
            }
        }
    }

    private static long readLimit() throws IOException {
        long limit = 0;
        while (limit < 1) {
            String line = reader.getString("Page limit :");
            try {
                limit = Mapper.mapLong(line);
            } catch (NotLongException e) {
                System.out.println("not a long");
            }
        }
        return limit;
    }

    public static void printCompanyList() throws SQLException, IOException {
        PageManager pageManager = null;
        pageManager = PageManager.createPageManager(readLimit());
        boolean tryAgain = true;
        while (tryAgain) {
            try {
                List<CompanyDTO> companies = null;
                try {
                    companies = getServiceFactory().getServiceCompany()
                            .getCompanies(pageManager.next());
                } catch (ProblemListException e) {
                    System.out.println("Absurd (page manager is bugged?)");
                    e.printStackTrace();
                    System.exit(1);
                }
                tryAgain = companies.size() != 0;
                companies.stream()
                        .forEach(company -> System.out.println(company));
            } catch (InvalidPageException e) {
                System.out.println("absurd");
                return;
            }
        }
    }

    public static boolean errorHandling(String message) throws IOException {
        System.out.println(message);
        System.out.println("1 - try again");
        return reader.getString("Anything else : Back to menu").equals("1");
    }

    public static void printComputer() throws IOException, SQLException {
        boolean tryAgain = true;
        while (tryAgain) {
            String iD = reader.getString("Computer ID :");
            try {
                getServiceFactory().getServiceComputer().getComputer(iD)
                        .ifPresentOrElse(
                                computer -> System.out.println(computer),
                                () -> System.out.println("No such computer"));
                tryAgain = false;
            } catch (NotLongException e) {
                tryAgain = errorHandling(iD + " is not a valid ID");
            }
        }
    }

    public static void deleteComputer() throws IOException, SQLException {
        boolean tryAgain = true;
        while (tryAgain) {
            String iD = reader.getString("Computer ID :");
            try {
                if (getServiceFactory().getServiceComputer()
                        .deleteComputer(iD)) {
                    System.out.println("Computer successfully deleted");
                } else {
                    System.out.println("No such computer");
                }
                tryAgain = false;
            } catch (NotLongException e) {
                tryAgain = errorHandling(iD + " is not a valid ID");
            }
        }
    }

    public static void createComputer() throws IOException, SQLException {
        boolean tryAgain = true;
        while (tryAgain) {
            String name = reader.getString("Computer name (mandatory) :");
            String introduced = reader.getString(
                    "Introduced the : (leave an empty line if no information");
            String discontinued = reader.getString("Discontinued the : "
                    + "(leave an empty line if no information");
            String companyID = reader.getString(
                    "Company ID : (leave an empty line if no information");
            String companyName = reader.getString(
                    "Company name : (leave an empty line if no information");
            ComputerDTO computer = ComputerDTO.builder().withName(name)
                    .withIntroduced(introduced).withDiscontinued(discontinued)
                    .withCompany(CompanyDTO.builder().withId(companyID)
                            .withName(companyName).build())
                    .build();
            try {
                getServiceFactory().getServiceComputer()
                        .createComputer(computer);
                tryAgain = false;
                System.out.println("Computer successfully created");
            } catch (ProblemListException e) {
                // TODO Auto-generated catch block
                System.out.println("issue creating computer");
                dealWithProblemList(e.getList());
                tryAgain = errorHandling("");
            } catch (NullComputerException e) {
                System.out.println(
                        "Stop trying to be clever (computer was null somehow)");
                tryAgain = errorHandling("");
            }
        }
    }

    public static void updateComputer() throws IOException, SQLException {
        boolean tryAgain = true;
        while (tryAgain) {
            String iD = reader.getString("Computer ID (mandatory) : ");
            String name = reader.getString("Computer name (mandatory) :");
            String introduced = reader.getString(
                    "Introduced the : (leave an empty line if no information");
            String discontinued = reader.getString("Discontinued the : "
                    + "(leave an empty line if no information");
            String companyID = reader.getString(
                    "Company ID : (leave an empty line if no information");
            String companyName = reader.getString(
                    "Company name : (leave an empty line if no information");
            ComputerDTO computer = ComputerDTO.builder().withName(name)
                    .withIntroduced(introduced).withDiscontinued(discontinued)
                    .withCompany(CompanyDTO.builder().withId(companyID)
                            .withName(companyName).build())
                    .build();
            try {
                if (getServiceFactory().getServiceComputer()
                        .updateComputer(computer)) {
                    System.out.println("Computer successfully updated");
                } else {
                    System.out.println("No such computer");
                }
                tryAgain = false;
            } catch (ProblemListException e) {
                System.out.println("issue updating computer");
                dealWithProblemList(e.getList());
                tryAgain = errorHandling("");
            } catch (NullComputerException e) {
                System.out.println(
                        "Stop trying to be clever (computer was null somehow)");
                tryAgain = errorHandling("");
            }
        }
    }

    private static void dealWithProblemList(List<Problem> list) {
        for (Problem problem : list) {
            switch (problem.getNature()) {
            case NOCOMPANY:
                System.out.println("No such company");
                break;
            case NOCOMPUTER:
                System.out.println("No such computer");
                break;
            case NONAME:
                System.out.println("Name is mandatory");
                break;
            case NOTADATE:
                System.out.println(problem.getOrigin().orElse("")
                        + " is not a valid date");
                break;
            case NOTALONG:
                System.out.println(
                        problem.getOrigin().orElse("") + " is not a valid ID");
                break;
            case WRONGORDER:
                System.out.println("Introduced and discontinued dates are in"
                        + " the wrong order");
                break;
            case BEFORE1970:
                System.out.println(problem.getOrigin().orElse("")
                        + " is not valid. Dates must be before 1970");
                break;
            default:
                throw new RuntimeException("Non exhaustive switch");
            }
        }

    }

}