package com.excilys.cdb.model.validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.exceptions.Problem;
import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.Computer.ComputerBuilder;

public class ComputerValidator implements Validator<ComputerBuilder> {

    @Override
    public void validate(ComputerBuilder builder) throws ProblemListException {
        List<Problem> problemList = new ArrayList<>();
        checkName(builder.getName(), problemList);
        checkDates(builder, problemList);
        if (problemList.size() > 0) {
            throw new ProblemListException(problemList);
        }
    }

    private void checkDates(ComputerBuilder builder,
            List<Problem> problemList) {
        LocalDateTime introduced = builder.getIntroduced();
        LocalDateTime discontinued = builder.getDiscontinued();
        if (checkDate(introduced, problemList)
                & checkDate(discontinued, problemList)) {
            checkAnteriority(introduced, discontinued, problemList);
        }
    }

    private void checkAnteriority(LocalDateTime introduced,
            LocalDateTime discontinued, List<Problem> problemList) {
        if (introduced.isAfter(discontinued)) {
            problemList.add(Problem.createWrongOrder());
        }
    }

    private boolean checkDate(LocalDateTime date, List<Problem> problemList) {
        if (date != null) {
            if (date.isBefore(LocalDateTime.parse("1970-01-01T00:00:01"))
                    || date.isAfter(
                            LocalDateTime.parse("2038-01-01T00:00:00"))) {
                problemList.add(Problem.createBefore1970(date.toString()));
            }
            return true;
        }
        return false;
    }

    private void checkName(String name, List<Problem> problemList) {
        if (name == null || name.contentEquals("")) {
            problemList.add(Problem.createNoName());
        }
    }
}
