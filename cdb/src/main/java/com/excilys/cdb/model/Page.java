package com.excilys.cdb.model;

import javax.annotation.Generated;

import com.excilys.cdb.exceptions.ProblemListException;
import com.excilys.cdb.model.validator.Validator;

public class Page {
    private long offset;
    private long limit;

    @Generated("SparkTools")
    private Page(PageBuilder builder) {
        this.offset = builder.offset;
        this.limit = builder.limit;
    }

    public long getOffset() {
        return offset;
    }

    public long getLimit() {
        return limit;
    }

    /**
     * default access for testing ONLY; use the builder to instantiate a page
     *
     * @param limit
     * @param offset
     */
    Page(long limit, long offset) {
        super();
        this.offset = offset;
        this.limit = limit;
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

    /**
     * Creates builder to build {@link Page}.
     *
     * @return created builder
     */
    @Generated("SparkTools")
    public static PageBuilder builder() {
        return new PageBuilder();
    }

    /**
     * Builder to build {@link Page}.
     */
    @Generated("SparkTools")
    public static final class PageBuilder {
        private long offset;
        private long limit;
        private static Validator<PageBuilder> validator;

        public static void setValidator(Validator<PageBuilder> validator) {
            PageBuilder.validator = validator;
        }

        private PageBuilder() {
        }

        public long getOffset() {
            return offset;
        }

        public long getLimit() {
            return limit;
        }

        public PageBuilder withOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public PageBuilder withLimit(long limit) {
            this.limit = limit;
            return this;
        }

        public Page build() throws ProblemListException {
            validator.validate(this);
            return new Page(this);
        }
    }

}
