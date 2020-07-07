package com.excilys.cdb.adapter.DAOAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Computer.ComputerBuilder;
import com.excilys.cdb.persistence.entity.EComputer;

@Component
public class ComputerMapper {

    @Autowired
    private CompanyMapper companyMapper;

    private ComputerMapper() {
    }

    public ComputerBuilder map(EComputer computer) {
        ComputerBuilder bob = Computer.builder().withName(computer.getName())
                .withID(computer.getId());
        if (computer.getCompany() != null) {
            bob.withCompany(companyMapper.map(computer.getCompany()));
        }
        DateMapper.toLocalDateTime(computer.getDiscontinued())
                .ifPresent(bob::withDiscontinued);
        DateMapper.toLocalDateTime(computer.getIntroduced())
                .ifPresent(bob::withIntroduced);
        return bob;
    }
}
