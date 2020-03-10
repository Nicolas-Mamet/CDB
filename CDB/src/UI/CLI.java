package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;
import model.Computer.ComputerBuilder;
import persistence.implementation.DAOFactory;




public final class CLI {
	
	private static DAOFactory daoFactory;
	private static BufferedReader reader;
	private static boolean ended;
	
	public static void main(String[] args) {
		daoFactory = new DAOFactory();
		reader = new BufferedReader(
				new InputStreamReader(System.in));
		ended = false;
		while(!ended) {
			try {
				switchLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (UserInterruptException e) {}
		}
		System.out.println("Good bye!");
	}
	
	private static final void switchLine()
			throws SQLException, IOException, UserInterruptException {
		String line = reader.readLine();
		switch (line) {
		case "list computers":
			listComputers();
			break;
		case "list companies":
			listCompanies();
			break;
		case "show computer":
			getComputer();
			break;
		case "create computer":
			createComputer();
			break;
		case "update computer":
			updateComputer();
			break;
		case "delete computer":
			deleteComputer();
			break;
		case "exit" :
		case "quit" :
			ended = true;
			break;
		default :
			System.out.println("default");
			displayHelp();
			switchLine();
		}
	}
	
	private static void displayHelp() {
		// TODO Auto-generated method stub
		
	}

	private static void deleteComputer() 
			throws IOException, SQLException, UserInterruptException {
		try {
			daoFactory.getComputerDAO().deleteComputer(readID());
			System.out.println("Successful deletion");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		switchLine();
		
	}

	private static long readID() throws IOException, UserInterruptException {
		boolean ok = false;
		long id = -1;
		while(!ok) {
			System.out.println("Computer ID :");
			try {
				String line = reader.readLine();
				if (line.contentEquals("")) {
					throw new UserInterruptException();
				}
				id = Long.parseLong(line);
				ok = true;
			} catch (NumberFormatException e) {
				System.out.println("Please enter a correct ID or "
						+ "type an empty line to quit");
			}
		}
		return id;
	}

	private static void updateComputer() 
			throws IOException, SQLException, UserInterruptException {
		Computer computer = initializeComputerWithoutID()
				.withId(readID()).build();
		try {
			daoFactory.getComputerDAO().createComputer(computer);
			System.out.println("Successful update");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
	}

	private static void createComputer()
			throws SQLException, IOException, UserInterruptException {
		Computer computer = initializeComputerWithoutID().build();
		try {
			daoFactory.getComputerDAO().createComputer(computer);
			System.out.println("Successful creation");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		switchLine();
	}


	private static ComputerBuilder initializeComputerWithoutID() {
		// TODO Auto-generated method stub
		return null;
	}

	private static void getComputer() 
			throws IOException, SQLException, UserInterruptException {
		long id = readID();
		Optional<Computer> oComputer =
				daoFactory.getComputerDAO().getComputer(id);
		if(oComputer.isEmpty()) {
			System.out.println("There is no computer with id equals to "
					+ id);
		} else {
			System.out.println(oComputer.get());
		}
		
		switchLine();
	}

	private static void listCompanies() 
			throws SQLException, IOException, UserInterruptException {
		List<Company> companies = 
				daoFactory.getCompanyDAO().getCompanies();
		for(Company company : companies) {
			System.out.println(company);
		}
		switchLine();
	}

	private static void listComputers() 
			throws SQLException, IOException, UserInterruptException {
		List<Computer> computers =
				daoFactory.getComputerDAO().getComputers();
		for(Computer computer : computers) {
			System.out.println(computer);
		}
		switchLine();
	}
	
}
