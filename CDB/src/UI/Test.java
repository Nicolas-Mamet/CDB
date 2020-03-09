package UI;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;
import persistence.CompanyDAO;
import persistence.ComputerDAO;

public class Test {
	
	private Test() {}
	
	public static void main(String[] args) {
		try {
			List<Computer> computers = ComputerDAO.getComputers();
			for(Computer computer : computers) {
				System.out.println(computer);
			}
			
			List<Company> companies = CompanyDAO.getCompanies();
			for(Company company : companies) {
				System.out.println(company);
			}
			
			Optional<Computer> computer = ComputerDAO.getComputer(1200L);
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
