package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.swing.undo.CannotRedoException;

import exceptions.NotLongException;
import exceptions.UserInterruptException;
import model.Company;
import model.Computer;
import model.Computer.ComputerBuilder;
import persistence.implementation.CompanyDAOImpl;
import persistence.implementation.ComputerDAOImpl;
import persistence.implementation.DAOFactory;
import services.implementation.AbstractDAOUser;
import services.implementation.ServiceFactory;
import services.interfaces.ServiceCompany;
import services.implementation.ServiceCompanyImpl;
import services.implementation.ServiceComputerImpl;




public final class CLI extends AbstractCanRead{
	 
	private static ReadFromCL reader;
	
	public static void setReader(ReadFromCL pReader) {
		reader = pReader;
	}
	
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
		String line = reader.getString("Enter an action :");
		switch (line) {
		case "list computers":
			ServiceCaller.printComputerList();
			break;
		case "list companies":
			ServiceCaller.printCompanyList();
			break;
		case "show computer":
			ServiceCaller.printComputer();
			break;
		case "create computer":
			ServiceCaller.createComputer();
			break;
		case "update computer":
			ServiceCaller.updateComputer();
			break;
		case "delete computer":
			ServiceCaller.deleteComputer();
			break;
		case "exit" :
		case "quit" :
			ended = true;
			break;
		default :
			System.out.println("default");
			//displayHelp();
			switchLine();
		}
	}

	private static void injectDependencies() {
		DAOFactory daoFactory = new DAOFactory();
		daoFactory.setCompanyDAO(new CompanyDAOImpl());
		daoFactory.setComputerDAO(new ComputerDAOImpl());
		AbstractDAOUser.setDAOFactory(daoFactory);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		AbstractCanRead.setReader(reader);
		//ReadFromCL.setReader(reader);
		ServiceFactory serviceFactory = new ServiceFactory();
		serviceFactory.setServiceCompany(new ServiceCompanyImpl());
		serviceFactory.setServiceComputer(new ServiceComputerImpl());
		AbstractServiceUser.setServiceFactory(serviceFactory);
		ReadFromCL readFromCL = new ReadFromCL();
		ServiceCaller.setReader(readFromCL);
		CLI.setReader(readFromCL);
		
	}
}
