package services.implementation;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import exceptions.NotLongException;
import mapper.Mapper;
import model.Company;
import model.Company.CompanyBuilder;
import services.interfaces.ServiceCompany;

public final class ServiceCompanyImpl extends AbstractDAOUser 
		implements ServiceCompany{
	
	@Override
	public List<Company> getCompanies() throws SQLException {
		return getDAOFactory().getCompanyDAO().getCompanies();
	}

	@Override
	public Optional<Company> getCompany(String stringID, String name)
			throws SQLException, NotLongException {
		long iD = Mapper.mapLong(stringID);
		CompanyBuilder bob = Company.builder().withID(iD);
		if(name != null && !name.equals("")) {
			bob.withName(name);
		}
		Company company = bob.build();
		if(!getDAOFactory().getCompanyDAO().checkCompany(company)) {
			return Optional.empty();
		}
		return Optional.of(company);
	}

}
