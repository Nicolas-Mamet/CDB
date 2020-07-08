package com.excilys.cdb.cli;

import java.io.IOException;

public class ReadFromCL extends AbstractCanRead {
    public String getString(String message) throws IOException {
        System.out.println(message);
        return getReader().readLine();
    }
}
