package ui;

import services.implementation.ServiceFactory;

public abstract class AbstractServiceUser {
	
	private static ServiceFactory serviceFactory;
	
	protected static final ServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	
	public static final void setDAOFactory(ServiceFactory pServiceFactory) {
		serviceFactory = pServiceFactory;
	}
}
