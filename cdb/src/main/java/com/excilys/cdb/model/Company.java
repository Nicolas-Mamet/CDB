package com.excilys.cdb.model;

import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.validator.Validator;

public final class Company {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * default access for testing ONLY; use the builder to instantiate a company
     *
     * @param id
     * @param name
     */
    Company(long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {
        private long id;
        private String name;
        private static Validator<CompanyBuilder> validator;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static void setValidator(Validator<CompanyBuilder> validator) {
            CompanyBuilder.validator = validator;
        }

        private CompanyBuilder() {
        }

        public CompanyBuilder withID(long id) {
            this.id = id;
            return this;
        }

        public CompanyBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Company build() throws ProblemListException {
            validator.validate(this);
            return new Company(this.id, this.name);
        }
    }

    @Override
    public String toString() {
        return "Company [(id : " + id + ")(name : " + name + ")]";
    }
}
