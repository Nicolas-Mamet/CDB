package com.excilys.cdb.exceptions;

import com.excilys.cdb.dto.PageDTO;

public class InvalidPageException extends Exception {

    final private PageDTO page;

    public PageDTO getPage() {
        return page;
    }

    public InvalidPageException() {
        page = null;
    }

    public InvalidPageException(PageDTO page) {
        this.page = page;
    }
}
