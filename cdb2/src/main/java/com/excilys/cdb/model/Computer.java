package com.excilys.cdb.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.validator.Validator;

public final class Computer {

    private long id;
    private String name;
    private LocalDateTime introduced;
    private LocalDateTime discontinued;
    private Company company;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getIntroduced() {
        return introduced;
    }

    public LocalDateTime getDiscontinued() {
        return discontinued;
    }

    public Company getCompany() {
        return company;
    }

    public static ComputerBuilder builder() {
        return new ComputerBuilder();
    }

    @Override
    public String toString() {
        return "Computer [ID=" + id + ", name=" + name + ", introduced="
                + introduced + ", discontinued=" + discontinued + ", company="
                + company + "]";
    }

    private Computer() {
    }

    public static class ComputerBuilder {
        private long id;
        private String name;
        private LocalDateTime introduced;
        private LocalDateTime discontinued;
        private Company company;

        /**
         * Performs validation before a computer is instantiated; should be
         * final but we use BuilderInitializer instead to facilitate DI and
         * testing. Should be effectively final.
         */
        private static Validator<ComputerBuilder> validator;

        /**
         * Should only be instanciated once by Spring; set the static validator
         * of the builder. Little trick to cope with Spring being unable to
         * autowire static fields
         *
         */
        @Component
        private static class BuilderInitializer {
            private BuilderInitializer(Validator<ComputerBuilder> validator) {
                ComputerBuilder.validator = validator;
            }
        }

        private ComputerBuilder() {
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getIntroduced() {
            return introduced;
        }

        public LocalDateTime getDiscontinued() {
            return discontinued;
        }

        public Company getCompany() {
            return company;
        }

        public ComputerBuilder withID(long id) {
            this.id = id;
            return this;
        }

        public ComputerBuilder withIntroduced(LocalDateTime introduced) {
            this.introduced = introduced;
            return this;
        }

        public ComputerBuilder withDiscontinued(LocalDateTime discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        public ComputerBuilder withCompany(Company company) {
            this.company = company;
            return this;
        }

        public ComputerBuilder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Throws a ProblemListException if the to-be-built company does not
         * pass the validation
         */
        public Computer build() throws ProblemListException {
            validator.validate(this);
            Computer computer = new Computer();
            computer.name = this.name;
            computer.company = this.company;
            computer.discontinued = this.discontinued;
            computer.introduced = this.introduced;
            computer.id = this.id;
            return computer;
        }
    }

}
