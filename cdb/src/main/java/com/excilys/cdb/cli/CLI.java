package com.excilys.cdb.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.excilys.cdb.exceptions.DBException;
import com.excilys.cdb.persistence.implementation.CompanyDAOImpl;
import com.excilys.cdb.persistence.implementation.ComputerDAOImpl;
import com.excilys.cdb.persistence.implementation.DAOFactory;
import com.excilys.cdb.persistence.implementation.HikariCP;
import com.excilys.cdb.persistence.interfaces.CompanyDAO;
import com.excilys.cdb.persistence.interfaces.ComputerDAO;
import com.excilys.cdb.persistence.interfaces.SQLDataSource;
import com.excilys.cdb.services.implementation.AbstractDAOUser;
import com.excilys.cdb.services.implementation.ServiceCompanyImpl;
import com.excilys.cdb.services.implementation.ServiceComputerImpl;
import com.excilys.cdb.services.implementation.ServiceFactory;

public final class CLI extends AbstractCanRead {

    private static ReadFromCL reader;

    public static void setReader(ReadFromCL pReader) {
        reader = pReader;
    }

    private static boolean ended;

    public static void main(String[] args) {
        injectDependencies();
        ended = false;
        while (!ended) {
            try {
                switchLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            } catch (DBException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Good bye!");
    }

    private static void switchLine() throws DBException, IOException {
        String line = reader.getString("Enter an action :");
        switch (line) {
        case "list computers":
            ServiceCaller.printComputerList();
            break;
        case "list companies":
            ServiceCaller.printCompanyList();
            break;
        case "show computer":
            ServiceCaller.printComputer();
            break;
        case "create computer":
            ServiceCaller.createComputer();
            break;
        case "update computer":
            ServiceCaller.updateComputer();
            break;
        case "delete computer":
            ServiceCaller.deleteComputer();
            break;
        case "exit":
        case "quit":
            ended = true;
            break;
        case "delete company":
            ServiceCaller.deleteCompany();
            break;
        default:
            System.out.println("default");
            // displayHelp();
            switchLine();
        }
    }

    private static void injectDependencies() {
        SQLDataSource dataSource = new HikariCP();
        CompanyDAO companyDAO = new CompanyDAOImpl();
        companyDAO.setDataSource(dataSource);
        ComputerDAO computerDAO = new ComputerDAOImpl();
        computerDAO.setDataSource(dataSource);
        DAOFactory daoFactory = new DAOFactory();
        daoFactory.setCompanyDAO(companyDAO);
        daoFactory.setComputerDAO(computerDAO);
        AbstractDAOUser.setDAOFactory(daoFactory);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        AbstractCanRead.setReader(reader);
        // ReadFromCL.setReader(reader);
        ServiceFactory serviceFactory = new ServiceFactory();
        ServiceCompanyImpl serviceCompany = new ServiceCompanyImpl();
        serviceFactory.setServiceCompany(serviceCompany);
        ServiceComputerImpl serviceComputer = new ServiceComputerImpl();
        serviceFactory.setServiceComputer(serviceComputer);
        AbstractServiceUser.setServiceFactory(serviceFactory);
        ReadFromCL readFromCL = new ReadFromCL();
        ServiceCaller.setReader(readFromCL);
        CLI.setReader(readFromCL);

    }
}
