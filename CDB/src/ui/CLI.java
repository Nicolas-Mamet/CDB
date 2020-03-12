package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.swing.undo.CannotRedoException;

import exceptions.UserInterruptException;
import model.Company;
import model.Computer;
import model.Computer.ComputerBuilder;
import persistence.implementation.DAOFactory;
import services.implementation.AbstractDAOUser;
import services.implementation.ServiceFactory;




public final class CLI {
	 
	private static boolean ended;
	
	public static void main(String[] args) {
		injectDependencies();
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

	private static void injectDependencies() {
		DAOFactory daoFactory = new DAOFactory();
		AbstractDAOUser.setDAOFactory(daoFactory);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		AbstractCanRead.setReader(reader);
		//ReadFromCL.setReader(reader);
		ServiceFactory serviceFactory = new ServiceFactory();
		AbstractServiceUser.setDAOFactory(serviceFactory);
		
	}
}
