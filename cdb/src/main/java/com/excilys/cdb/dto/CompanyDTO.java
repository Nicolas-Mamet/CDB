package com.excilys.cdb.dto;

public class CompanyDTO {
    private String id;
    private String name;

    private CompanyDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Creates builder to build {@link CompanyDTO}.
     * 
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link CompanyDTO}.
     */
    public static final class Builder {
        private String id;
        private String name;

        private Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public CompanyDTO build() {
            return new CompanyDTO(this);
        }
    }
}
