package com.excilys.cdb.model.validator;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Company.CompanyBuilder;

@Component
public class CompanyValidator implements ICompanyValidator {

    private CompanyValidator() {
    }

    @Override
    public void validate(CompanyBuilder builder) throws ProblemListException {
        if (builder.getId() < 1) {
            throw new ProblemListException(Arrays
                    .asList(Problem.createNoCompany(builder.getId() + "")));
        }
    }

}
