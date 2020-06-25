package com.excilys.cdb.exceptions;

import java.util.List;

public class CorruptComputerException extends Exception {

    private final List<Problem> problemList;

    public CorruptComputerException(List<Problem> problemList) {
        this.problemList = problemList;
    }

    public List<Problem> getProblemList() {
        return problemList;
    }
}
