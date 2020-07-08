package com.excilys.cdb.model.validator;

import com.excilys.cdb.crossProject.exceptions.ProblemListException;

@FunctionalInterface
public interface Validator<T> {
    void validate(T t) throws ProblemListException;
}
