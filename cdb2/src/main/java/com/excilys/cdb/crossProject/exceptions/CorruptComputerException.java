package com.excilys.cdb.crossProject.exceptions;

import java.util.List;

public class CorruptComputerException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final List<Problem> problemList;

    public CorruptComputerException(List<Problem> problemList) {
        this.problemList = problemList;
    }

    public List<Problem> getProblemList() {
        return problemList;
    }
}
