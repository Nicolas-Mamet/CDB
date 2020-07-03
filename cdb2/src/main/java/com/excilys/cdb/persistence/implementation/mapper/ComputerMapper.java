package com.excilys.cdb.persistence.implementation.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.mapper.Mapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.persistence.model.EComputer;

@Component
public class ComputerMapper {

    @Autowired
    private CompanyMapper companyMapper;

    private ComputerMapper() {
    }

    public ComputerBuilder map(EComputer computer) {
        ComputerBuilder bob = Computer.builder().withName(computer.getName())
                .withID(computer.getId())
                .withCompany(companyMapper.map(computer.getCompany()));
        Mapper.toLocalDateTime(computer.getDiscontinued())
                .ifPresent(bob::withDiscontinued);
        Mapper.toLocalDateTime(computer.getIntroduced())
                .ifPresent(bob::withIntroduced);
        return bob;
    }
}
