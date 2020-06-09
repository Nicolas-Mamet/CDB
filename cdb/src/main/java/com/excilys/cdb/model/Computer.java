package com.excilys.cdb.model;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public final class Computer {

    private long iD;
    private String name;
    private LocalDateTime introduced;
    private LocalDateTime discontinued;
    private Company company;

    public long getID() {
        return iD;
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

    private Computer() {
    }

    public static class ComputerBuilder {
        private long iD;
        private String name;
        private LocalDateTime introduced;
        private LocalDateTime discontinued;
        private Company company;

        private ComputerBuilder() {
        }

        public ComputerBuilder withID(long iD) {
            this.iD = iD;
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

        public Computer build() {
            Computer computer = new Computer();
            computer.name = this.name;
            computer.company = this.company;
            computer.discontinued = this.discontinued;
            computer.introduced = this.introduced;
            computer.iD = this.iD;
            return computer;
        }
    }

    @Override
    public String toString() {
        StringBuilder bob = new StringBuilder().append("Computer[");
        for (Field f : Computer.class.getDeclaredFields()) {
            try {
                // check access before modifying the builder
                Object value = f.get(this);
                bob.append("(");
                bob.append(f.getName()).append(" : ").append(value);
                bob.append(")");
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("absurd?");
            } catch (IllegalAccessException e) {
            }
        }
        bob.append("]");
        return bob.toString();
    }

}
