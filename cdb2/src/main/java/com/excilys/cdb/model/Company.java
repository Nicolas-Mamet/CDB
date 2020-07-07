package com.excilys.cdb.model;

import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.exceptions.ProblemListException;
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
    private Company(long id, String name) {
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

        /**
         * Performs validation before a company is instantiated; should be final
         * but we use BuilderInitializer instead to facilitate DI and testing.
         * Should be effectively final.
         */
        private static Validator<CompanyBuilder> validator;

        /**
         * Should only be instanciated once by Spring; set the static validator
         * of the builder. Little trick to cope with Spring being unable to
         * autowire static fields
         *
         */
        @Component
        private static class BuilderInitializer {
            private BuilderInitializer(Validator<CompanyBuilder> validator) {
                CompanyBuilder.validator = validator;
            }
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
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

        /**
         * Throws a ProblemListException if the to-be-built company does not
         * pass the validation
         */
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
