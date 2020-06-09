package com.excilys.cdb.services.implementation;

import com.excilys.cdb.cli.AbstractServiceUser;
import com.excilys.cdb.persistence.implementation.DAOFactory;

public abstract class AbstractDAOUser extends AbstractServiceUser {

    private static DAOFactory daoFactory;

    protected static final DAOFactory getDAOFactory() {
        return daoFactory;
    }

    public static final void setDAOFactory(DAOFactory pDAOFactory) {
        daoFactory = pDAOFactory;
    }
}
