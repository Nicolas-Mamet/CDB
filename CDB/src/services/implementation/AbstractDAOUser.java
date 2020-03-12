package services.implementation;

import persistence.implementation.DAOFactory;

public abstract class AbstractDAOUser {
	
	private static DAOFactory daoFactory;
	
	protected static final DAOFactory getDAOFactory() {
		return daoFactory;
	}
	
	public static final void setDAOFactory(DAOFactory pDAOFactory) {
		daoFactory = pDAOFactory;
	}
}
