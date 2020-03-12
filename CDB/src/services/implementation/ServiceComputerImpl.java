package services.implementation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import exceptions.NoNameComputerException;
import exceptions.WrongDatesException;
import model.Computer;
import services.interfaces.ServiceComputer;

class ServiceComputerImpl extends AbstractDAOUser implements ServiceComputer{
	
	private static final ServiceComputerImpl INSTANCE = 
			new ServiceComputerImpl();
	
	public static ServiceComputerImpl getInstance() { return INSTANCE; }
	
	@Override
	public List<Computer>  getComputers() throws SQLException{
		return getDAOFactory().getComputerDAO().getComputers();
	}
	
	@Override
	public Optional<Computer> getComputer(long id) throws SQLException{
		return getDAOFactory().getComputerDAO().getComputer(id);
	}
	
	@Override
	public void deleteComputer(long id) throws SQLException {
		getDAOFactory().getComputerDAO().deleteComputer(id);
	}
	
	private void checkComputerName(Computer computer) 
			throws NoNameComputerException {
		if(computer.getName() == null) {
			throw new NoNameComputerException();
		}
	}
	
	private void checkComputerDate(Computer computer)
			throws WrongDatesException {
		if(computer.getIntroduced() != null
				&& computer.getDiscontinued() != null) {
			if(computer.getIntroduced().isAfter(computer.getDiscontinued())) {
				throw new WrongDatesException();
			}
		}
	}
	
	private void checkComputer(Computer computer)
			throws WrongDatesException, NoNameComputerException, SQLException {
		checkComputerDate(computer);
		checkComputerName(computer);
		ServiceCompanyImpl.getInstance().checkCompany(computer.getCompany());
	}
	
	@Override
	public void createComputer(Computer computer)
			throws SQLException, WrongDatesException, NoNameComputerException {
		checkComputer(computer);
		getDAOFactory().getComputerDAO().createComputer(computer);
	}

	@Override
	public void updateComputer(Computer computer)
			throws SQLException, WrongDatesException, NoNameComputerException {
		checkComputer(computer);
		getDAOFactory().getComputerDAO().updateComputer(computer);
	}

	
}
