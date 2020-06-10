package services.implementation;

import services.interfaces.ServiceCompany;
import services.interfaces.ServiceComputer;

public class ServiceFactory {

    private ServiceComputer serviceComputer;
    private ServiceCompany serviceCompany;

    public ServiceComputer getServiceComputer() {
        return serviceComputer;
    }

    public ServiceCompany getServiceCompany() {
        return serviceCompany;
    }

    public void setServiceComputer(ServiceComputer serviceComputer) {
        this.serviceComputer = serviceComputer;
    }

    public void setServiceCompany(ServiceCompany serviceCompany) {
        this.serviceCompany = serviceCompany;
    }
}
