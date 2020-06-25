package com.excilys.cdb.model.validator;

import com.excilys.cdb.exceptions.ProblemListException;

@FunctionalInterface
public interface Validator<T> {
    void validate(T t) throws ProblemListException;
}
