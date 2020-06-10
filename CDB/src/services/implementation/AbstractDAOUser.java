package services.implementation;

import persistence.implementation.DAOFactory;
import ui.AbstractServiceUser;

public abstract class AbstractDAOUser extends AbstractServiceUser {

    private static DAOFactory daoFactory;

    protected static final DAOFactory getDAOFactory() {
        return daoFactory;
    }

    public static final void setDAOFactory(DAOFactory pDAOFactory) {
        daoFactory = pDAOFactory;
    }
}
