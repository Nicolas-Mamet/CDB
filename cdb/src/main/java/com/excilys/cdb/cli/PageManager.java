package com.excilys.cdb.cli;

import com.excilys.cdb.dto.PageDTO;
import com.excilys.cdb.exceptions.FirstPageException;

import util.GeneralUtil;

public class PageManager {
    private long limit;
    private long offset;

    public PageDTO next() {
        offset += limit;
        return PageDTO.builder().withLimit(GeneralUtil.toString(limit))
                .withOffset(GeneralUtil.toString(offset)).build();
    }

    public PageDTO previous() throws FirstPageException {
        if (offset - limit < 0) {
            throw new FirstPageException();
        } else {
            offset -= limit;
            return PageDTO.builder().withLimit(GeneralUtil.toString(limit))
                    .withOffset(GeneralUtil.toString(offset)).build();
        }
    }

    private PageManager(long limit) {
        this.limit = limit;
        offset = -limit;
    }

    public static PageManager createPageManager(long limit) {
        return new PageManager(limit);
    }

}
