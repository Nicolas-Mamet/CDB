package com.excilys.cdb.services.implementation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.exceptions.InvalidCompanyException;
import com.excilys.cdb.exceptions.NotLongException;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.mapper.MapperDTO;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.services.interfaces.ServiceCompany;

public final class ServiceCompanyImpl extends AbstractDAOUser
        implements ServiceCompany {

    @Override
    public List<CompanyDTO> getCompanies(PageDTO pageDTO)
            throws ProblemListException, DBException {
        Page page = MapperDTO.DTOToPage(pageDTO).orElse(null);
        try {
            return getDAOFactory().getCompanyDAO().getPageOfCompanies(page)
                    .stream().filter(c -> c != null)
                    .map(c -> MapperDTO.CompanyToDTO(c).get())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<String> getCompanyName(long iD) throws DBException {
        try {
            return getDAOFactory().getCompanyDAO().getCompanyName(iD);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public List<CompanyDTO> getCompanies() throws DBException {
        try {
            return getDAOFactory().getCompanyDAO().getCompanies().stream()
                    .filter(c -> c != null)
                    .map(c -> MapperDTO.CompanyToDTO(c).get())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public CompanyDTO getCompany(String iDString)
            throws NotLongException, InvalidCompanyException, DBException {
        long iD = Mapper.mapLong(iDString);
        try {
            Optional<String> name =
                    getDAOFactory().getCompanyDAO().getCompanyName(iD);
            return CompanyDTO.builder().withId(iDString)
                    .withName(name.orElseThrow(InvalidCompanyException::new))
                    .build();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean deleteCompany(String idString)
            throws NotLongException, InvalidCompanyException, DBException {
        long id = Mapper.mapLong(idString);
        try {
            boolean ok = getDAOFactory().getCompanyDAO().deleteCompany(id);
            if (ok) {
                return ok;
            } else {
                throw new InvalidCompanyException();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
