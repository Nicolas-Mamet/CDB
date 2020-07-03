package com.excilys.cdb.cli;

import java.io.IOException;

import com.excilys.cdb.exceptions.DBException;

public final class CLI extends AbstractCanRead {

    private static ReadFromCL reader;

    public static void setReader(ReadFromCL pReader) {
        reader = pReader;
    }

    private static boolean ended;

    public static void main(String[] args) {
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
}
