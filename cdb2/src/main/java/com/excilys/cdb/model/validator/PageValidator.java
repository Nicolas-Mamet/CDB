package com.excilys.cdb.model.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Page.PageBuilder;

@Component
public class PageValidator implements IPageValidator {

    private PageValidator() {
    }

    @Override
    public void validate(PageBuilder builder) throws ProblemListException {
        List<Problem> problems = new ArrayList<>();
        if (builder.getOffset() < 0) {
            problems.add(Problem.createWrongOffset(builder.getOffset()));
        }
        if (builder.getLimit() < 1) {
            problems.add(Problem.createWrongLimit(builder.getLimit()));
        }
        if (problems.size() > 0) {
            throw new ProblemListException(problems);
        }
    }

}
