package com.excilys.cdb.crossProject;

import com.excilys.cdb.crossProject.exceptions.NotLongException;

public class Mapper {

    private Mapper() {
    }

    /**
     * Parse the parameter into a long; raise an exception if the parameter is
     * null or does not represent a long.
     *
     * @param string
     * @return
     * @throws NotLongException
     */
    public static Long mapLong(String string) throws NotLongException {
        long iD;
        try {
            iD = Long.parseLong(string);
        } catch (NumberFormatException e) {
            throw new NotLongException();
        }
        return iD;
    }
}
