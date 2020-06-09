package com.excilys.cdb.persistence.implementation;

import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;

public class DAOFactory {

    private CompanyDAO companyDAO;
    private ComputerDAO computerDAO;

    public CompanyDAO getCompanyDAO() {
        return companyDAO;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public ComputerDAO getComputerDAO() {
        return computerDAO;
    }

    public void setComputerDAO(ComputerDAO computerDAO) {
        this.computerDAO = computerDAO;
    }
}
