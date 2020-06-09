package com.excilys.cdb.services.implementation;

import com.excilys.cdb.services.interfaces.ServiceCompany;
import com.excilys.cdb.services.interfaces.ServiceComputer;

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
