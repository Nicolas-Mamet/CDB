package com.excilys.cdb.cli;

import com.excilys.cdb.services.implementation.ServiceFactory;

public abstract class AbstractServiceUser {

    private static ServiceFactory serviceFactory;

    protected static final ServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    public static final void setServiceFactory(ServiceFactory pServiceFactory) {
        serviceFactory = pServiceFactory;
    }
}
