package com.excilys.cdb.exceptions;

public class DBException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DBException(Exception e) {
        super(e);
    }
}
