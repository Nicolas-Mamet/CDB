package UI;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;
import persistence.implementation.CompanyDAOImpl;
import persistence.implementation.ComputerDAOImpl;
import persistence.implementation.DAOFactory;
import model.Computer.ComputerBuilder;

public class Test {
	
	private Test() {}
	
	public static void main(String[] args) {
		DAOFactory daoFactory = new DAOFactory();
		try {
			List<Computer> computers =
					daoFactory.getComputerDAO().getComputers();
			for(Computer computer : computers) {
				System.out.println(computer);
			}
			
			List<Company> companies = 
					daoFactory.getCompanyDAO().getCompanies();
			for(Company company : companies) {
				System.out.println(company);
			}
			/*
			Computer c1 = new ComputerBuilder("Ordinateur de bob").build();
			daoFactory.getComputerDAO().createComputer(c1);
			*/
			
			Computer c2 = new ComputerBuilder("Ordinateur de bob 3")
					.withId(577).build();
			daoFactory.getComputerDAO().updateComputer(c2);
			daoFactory.getComputerDAO().deleteComputer(c2.getId());
			
			Optional<Computer> computer = 
					daoFactory.getComputerDAO().getComputer(577);
			if(computer.isEmpty()) {
				System.out.println("pas trouvé");
			} else {
				System.out.println(computer.get());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
