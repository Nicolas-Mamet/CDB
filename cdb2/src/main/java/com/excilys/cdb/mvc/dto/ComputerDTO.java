package com.excilys.cdb.mvc.dto;

public class ComputerDTO {

    private String id;
    private String name;
    private String introduced;
    private String discontinued;
    private CompanyDTO company;

    private ComputerDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.company = builder.company;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduced() {
        return introduced;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    @Override
    public String toString() {
        return "ComputerDTO [iD=" + id + ", name=" + name + ", introduced="
                + introduced + ", discontinued=" + discontinued + ", company="
                + company + "]";
    }

    /**
     * Creates builder to build {@link ComputerDTO}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link ComputerDTO}.
     */
    public static final class Builder {
        private String id;
        private String name;
        private String introduced;
        private String discontinued;
        private CompanyDTO company;

        private Builder() {
        }

        public Builder withId(String iD) {
            this.id = iD;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withIntroduced(String introduced) {
            this.introduced = introduced;
            return this;
        }

        public Builder withDiscontinued(String discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        public Builder withCompany(CompanyDTO company) {
            this.company = company;
            return this;
        }

        public ComputerDTO build() {
            return new ComputerDTO(this);
        }
    }
}
