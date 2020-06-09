package com.excilys.cdb.exceptions;

import java.util.List;

public class ProblemListException extends Exception {

    List<Problem> list;

    public ProblemListException(List<Problem> list) {
        super();
        this.list = list;
    }

    public List<Problem> getList() {
        return list;
    }
}
