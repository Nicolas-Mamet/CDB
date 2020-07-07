package com.excilys.cdb.adapter.DAOAdapter;

import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.exceptions.AbsurdException;
import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.entity.ECompany;

@Component
public class CompanyMapper {
    public Company map(ECompany company) {
        try {
            return Company.builder().withID(company.getId())
                    .withName(company.getName()).build();
        } catch (ProblemListException e) {
            throw new AbsurdException("Could not instantiate a company;"
                    + " the database schema should" + " prevent this");
        }
    }
}
