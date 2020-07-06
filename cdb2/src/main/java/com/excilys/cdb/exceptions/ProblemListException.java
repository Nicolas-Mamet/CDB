package com.excilys.cdb.exceptions;

import java.util.List;

public class ProblemListException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    List<Problem> list;

    public ProblemListException(List<Problem> list) {
        super();
        this.list = list;
    }

    public List<Problem> getList() {
        return list;
    }
}
