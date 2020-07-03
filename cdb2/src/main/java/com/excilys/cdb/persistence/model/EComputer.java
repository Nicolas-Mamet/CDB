package com.excilys.cdb.persistence.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.excilys.cdb.exceptions.AbsurdOptionalException;
import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Computer;

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

    private EComputer(Computer computer, ECompany company) {
        this(computer);
        this.company = company;
    }

    private EComputer(Computer computer) {
        if (computer.getId() > 0) {
            this.id = computer.getId();
        }
        this.name = computer.getName();
        this.introduced = Mapper.valueOf(computer.getIntroduced())
                .orElseThrow(AbsurdOptionalException::new);
        this.discontinued = Mapper.valueOf(computer.getDiscontinued())
                .orElseThrow(AbsurdOptionalException::new);
        this.company = null;
    }

    public static EComputer createEComputer(Computer computer,
            ECompany company) {
        if (company == null) {
            return new EComputer(computer);
        } else {
            return new EComputer(computer, company);
        }
    }
}
