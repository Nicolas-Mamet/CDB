package com.excilys.cdb.persistence.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "computer")
public class EComputer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Timestamp introduced;
    private Timestamp discontinued;

    @ManyToOne
    private ECompany company;

    private EComputer(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.introduced = builder.introduced;
        this.discontinued = builder.discontinued;
        this.company = builder.company;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getIntroduced() {
        return introduced;
    }

    public Timestamp getDiscontinued() {
        return discontinued;
    }

    public ECompany getCompany() {
        return company;
    }

    protected EComputer() {
    }

    /**
     * Creates builder to build {@link EComputer}.
     * 
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link EComputer}.
     */
    public static final class Builder {
        private Long id;
        private String name;
        private Timestamp introduced;
        private Timestamp discontinued;
        private ECompany company;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withIntroduced(Timestamp introduced) {
            this.introduced = introduced;
            return this;
        }

        public Builder withDiscontinued(Timestamp discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        public Builder withCompany(ECompany company) {
            this.company = company;
            return this;
        }

        public EComputer build() {
            return new EComputer(this);
        }
    }

}
