package com.excilys.cdb.adapter.DAOAdapter;

import org.springframework.stereotype.Component;

import com.excilys.cdb.persistence.Page;

@Component
public class PageBuilderDirector {
    public Page buildPage(com.excilys.cdb.model.Page pageModel) {
        return Page.builder().withLimit(pageModel.getLimit())
                .withOffset(pageModel.getOffset()).build();
    }
}
