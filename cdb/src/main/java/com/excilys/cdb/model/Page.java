package com.excilys.cdb.model;

public class Page {
    private long offset;
    private long limit;

    public long getOffset() {
        return offset;
    }

    public long getLimit() {
        return limit;
    }

    private Page(long limit, long offset) {
        super();
        this.offset = offset;
        this.limit = limit;
    }

    public static Page createPage(long limit, long offset) {
        return new Page(limit, offset);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Page)) {
            return false;
        }
        Page p = (Page) o;
        return offset == p.offset && limit == p.limit;
    }
}
