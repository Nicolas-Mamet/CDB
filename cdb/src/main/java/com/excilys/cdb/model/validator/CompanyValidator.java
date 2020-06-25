package com.excilys.cdb.model.validator;

import java.util.Arrays;

import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company.CompanyBuilder;

public class CompanyValidator implements Validator<CompanyBuilder> {

    @Override
    public void validate(CompanyBuilder builder) throws ProblemListException {
        if (builder.getId() < 1) {
            throw new ProblemListException(Arrays
                    .asList(Problem.createNoCompany(builder.getId() + "")));
        }
    }

}
