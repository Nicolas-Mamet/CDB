package com.excilys.cdb.adapter.DAOAdapter;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.entity.ECompany;
import com.excilys.cdb.persistence.entity.EComputer;
import com.excilys.cdb.persistence.entity.EComputer.Builder;

@Component
public class EComputerBuilderDirector {
    public EComputer buildComputer(Computer computer, ECompany company) {
        Builder bob = build(computer);
        if (company != null) {
            bob.withCompany(company);
        }
        return bob.build();
    }

    private Builder build(Computer computer) {
        Builder bob = EComputer.builder().withName(computer.getName())
                .withIntroduced(
                        DateMapper.valueOf(computer.getIntroduced())
                                .orElse(null))
                .withDiscontinued(DateMapper.valueOf(computer.getDiscontinued())
                        .orElse(null));
        if (computer.getId() > 0) {
            bob.withId(computer.getId());
        }
        return bob;
    }
}
