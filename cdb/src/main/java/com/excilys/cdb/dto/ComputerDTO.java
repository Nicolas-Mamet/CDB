package com.excilys.cdb.dto;

public class ComputerDTO {
    private String iD;
    private String name;
    private String introduced;
    private String discontinued;
    private CompanyDTO company;

    private ComputerDTO(Builder builder) {
        this.iD = builder.iD;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.company = builder.company;
    }

    public String getID() {
        return iD;
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
        private String iD;
        private String name;
        private String introduced;
        private String discontinued;
        private CompanyDTO company;

        private Builder() {
        }

        public Builder withID(String iD) {
            this.iD = iD;
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
