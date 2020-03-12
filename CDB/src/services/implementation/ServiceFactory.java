package services.implementation;

import services.interfaces.ServiceCompany;
import services.interfaces.ServiceComputer;

public class ServiceFactory {
	
	public ServiceComputer getComputerService() {
		return ServiceComputerImpl.getInstance();
	}
	
	public ServiceCompany getCompanyService() {
		return ServiceCompanyImpl.getInstance();
	}
}
