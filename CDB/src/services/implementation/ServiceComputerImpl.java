package services.implementation;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.function.Supplier;

import exceptions.ProblemListException;
import exceptions.NotDateException;
import exceptions.NotLongException;
import exceptions.Problem;
import mapper.Mapper;
import model.Company;
import model.Computer;
import services.interfaces.ServiceComputer;
import model.Computer.ComputerBuilder;

public class ServiceComputerImpl extends AbstractDAOUser implements ServiceComputer{
	
	@Override
	public List<Computer>  getComputers() throws SQLException{
		return getDAOFactory().getComputerDAO().getComputers();
	}
	
	@Override
	public Optional<Computer> getComputer(String string)
			throws SQLException, NotLongException{
		long id = Mapper.mapLong(string);
		return getDAOFactory().getComputerDAO().getComputer(id);
	}
	
	@Override
	public boolean deleteComputer(String string)
			throws SQLException, NotLongException {
		long id = Mapper.mapLong(string);
		return getDAOFactory().getComputerDAO().deleteComputer(id);
	}
	
	@Override
	public void createComputer(
			String name,
			String introduced,
			String discontinued,
			String companyID,
			String companyName)
			throws SQLException, ProblemListException {
		Computer computer =buildComputerNoID(
				name,
				introduced,
				discontinued,
				companyID,
				companyName);
		getDAOFactory().getComputerDAO().createComputer(computer);
	}
	
	@Override
	public boolean updateComputer(
			String iD,
			String name,
			String introduced,
			String discontinued,
			String companyID,
			String companyName)
			throws SQLException, ProblemListException {
		Computer computer = buildComputerWithID(
				iD,
				name,
				introduced,
				discontinued,
				companyID,
				companyName);
		return getDAOFactory().getComputerDAO().updateComputer(computer);
	}

	private Computer buildComputerWithID(
			String iD,
			String name,
			String introduced,
			String discontinued,
			String companyID,
			String companyName) 
			throws SQLException, ProblemListException {
		List<Problem> problemList = new ArrayList<Problem>();
		ComputerBuilder bob = Computer.builder().withName(name);
		checkAndBuildID(problemList, bob, iD);
		checkAndBuildName(problemList, bob, name);
		checkAndBuildDates(problemList, bob, introduced, discontinued);
		checkAndBuildCompany(problemList, bob, companyID, companyName);
		conditionalThrowList(problemList);
		return bob.build();
	}

	private Computer buildComputerNoID(
			String name,
			String introduced,
			String discontinued,
			String companyID,
			String companyName)
			throws SQLException, ProblemListException {
		List<Problem> problemList = new ArrayList<Problem>();
		ComputerBuilder bob = Computer.builder().withName(name);
		checkAndBuildName(problemList, bob, name);
		checkAndBuildDates(problemList, bob, introduced, discontinued);
		checkAndBuildCompany(problemList, bob, companyID, companyName);
		conditionalThrowList(problemList);
		return bob.build();
	}
	

	private void checkAndBuildID(
			List<Problem> problemList,
			ComputerBuilder bob,
			String iD) throws SQLException {
		try {
			long iDLong = Mapper.mapLong(iD);
			if(getDAOFactory().getComputerDAO().getComputer(iDLong)
					.isPresent())
			{
				bob.withID(iDLong);
			}
		} catch(NotLongException e) {
			problemList.add(Problem.createNotALong(iD));
		}
		
	}
	
	private void checkAndBuildName(
			List<Problem> list,
			ComputerBuilder bob,
			String name) {
		if(name == null) {
			list.add(Problem.createNoName());
		}
	}

	private void checkAndBuildDates(
			List<Problem> problemList,
			ComputerBuilder bob,
			String introduced,
			String discontinued) {
		int count = problemList.size();
		Optional<LocalDateTime> intro = getDate(problemList, introduced);
		Optional<LocalDateTime> disco = getDate(problemList, discontinued);
		if(count == problemList.size()) {
			checkComputerDates(problemList, intro, disco);
		}
		intro.ifPresent(date -> bob.withIntroduced(date));
		disco.ifPresent(date -> bob.withDiscontinued(date));
	}

	private Optional<LocalDateTime> getDate(
			List<Problem> problemList,
			String date) {
		try {
			return Mapper.mapLocalDateTime(date);
		} catch (NotDateException e) {
			problemList.add(Problem.createNotADate(date));
			return Optional.empty();
		}
	}

	private void checkComputerDates(
			List<Problem> problemList,
			Optional<LocalDateTime> introduced,
			Optional<LocalDateTime> discontinued){
		if(introduced.isPresent()
					&& discontinued.isPresent()
					&& introduced.get().isAfter(discontinued.get())) {
			problemList.add(Problem.createWrongOrder());
		}
	}
	
	private void checkAndBuildCompany(
			List<Problem> problemList,
			ComputerBuilder bob,
			String companyID, 
			String companyName) throws SQLException {
		try {
			if(companyID != null && companyName != null) {
				if(companyID.equals("") && companyName.equals("")) {
					return;
				}
				Optional<Company> company = getServiceFactory()
						.getServiceCompany()
						.getCompany(companyID, companyName);
				if(company.isPresent()) {
					bob.withCompany(company.get());
				} else {
					problemList.add(Problem.createNoCompany(
							companyID+" "+companyName));
				}
			}
		} catch (NotLongException e) {
			problemList.add(Problem.createNotALong(companyID));
		}
	}
	
	private void conditionalThrowList(List<Problem> problemList)
			throws ProblemListException {
		if(!problemList.isEmpty()) {
			throw new ProblemListException(problemList);
		}
	}
}
