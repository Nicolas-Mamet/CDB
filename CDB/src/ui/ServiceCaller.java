package ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import exceptions.NotLongException;
import exceptions.Problem;
import exceptions.ProblemListException;
import model.Company;
import model.Computer;

public class ServiceCaller extends AbstractServiceUser {
	
	private static ReadFromCL reader;
	
	public static void setReader(ReadFromCL reader) {
		ServiceCaller.reader = reader;
	}

	public static void printComputerList() throws SQLException {
		
		for(Computer computer : 
			getServiceFactory().getServiceComputer().getComputers()) {
			System.out.println(computer);
		}
	}
	
	public static void printCompanyList() throws SQLException {
		for(Company company : 
			getServiceFactory().getServiceCompany().getCompanies()) {
			System.out.println(company);
		}
	}
	
	public static boolean errorHandling(String message) throws IOException {
		System.out.println(message);
		System.out.println("1 - try again");
		return reader.getString("Anything else : Back to menu").equals("1");
	}
	
	public static void printComputer() throws IOException, SQLException {
		boolean tryAgain = true;
		while(tryAgain ) {
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
		while(tryAgain ) {
			String iD = reader.getString("Computer ID :");
			try {
				if(getServiceFactory().getServiceComputer().deleteComputer(iD)){
					System.out.println("Computer successfully deleted");
				} else {
					System.out.println("No such computer");
				}
				tryAgain = false;;
			} catch (NotLongException e) {
				tryAgain = errorHandling(iD + " is not a valid ID");
			}
		}
	}
	
	public static void createComputer() throws IOException, SQLException {
		boolean tryAgain = true;
		while(tryAgain) {
			String name = reader.getString("Computer name (mandatory) :");
			String introduced = reader.getString(
					"Introduced the : (leave an empty line if no information");
			String discontinued = reader.getString(
					"Discontinued the : "
					+ "(leave an empty line if no information");
			String companyID = reader.getString(
					"Company ID : (leave an empty line if no information");
			String companyName = reader.getString(
					"Company name : (leave an empty line if no information");
			try {
				getServiceFactory().getServiceComputer().createComputer(
						name, introduced, discontinued, companyID, companyName);
				tryAgain = false;
				System.out.println("Computer successfully created");
			} catch (ProblemListException e) {
				// TODO Auto-generated catch block
				System.out.println("issue creating computer");
				dealWithProblemList(e.getList());
				tryAgain = errorHandling("");
			}
		}
	}
	
	public static void updateComputer() throws IOException, SQLException {
		boolean tryAgain = true;
		while(tryAgain) {
			String iD = reader.getString("Computer ID (mandatory) : ");
			String name = reader.getString("Computer name (mandatory) :");
			String introduced = reader.getString(
					"Introduced the : (leave an empty line if no information");
			String discontinued = reader.getString(
					"Discontinued the : "
					+ "(leave an empty line if no information");
			String companyID = reader.getString(
					"Company ID : (leave an empty line if no information");
			String companyName = reader.getString(
					"Company name : (leave an empty line if no information");
			try {
				if(getServiceFactory().getServiceComputer().updateComputer(
						iD,
						name,
						introduced,
						discontinued,
						companyID,
						companyName)) {
					System.out.println("Computer successfully updated");
				} else {
					System.out.println("No such computer");
				}
				tryAgain = false;
			} catch (ProblemListException e) {
				System.out.println("issue updating computer");
				dealWithProblemList(e.getList());
				tryAgain = errorHandling("");
			}
		}
	}
	
	private static void dealWithProblemList(List<Problem> list) {
		for(Problem problem : list) {
			switch(problem.getNature()) {
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
				System.out.println(
						problem.getOrigin().orElse("")+" is not a valid date");
				break;
			case NOTALONG:
				System.out.println(
						problem.getOrigin().orElse("")+" is not a valid ID");
				break;
			case WRONGORDER:
				System.out.println("Introduced and discontinued dates are in"
						+ "the wrong order");
				break;
			default:
				break;
			
			}
		}
		
	}

}
