package com.excilys.cdb.model;

import org.springframework.stereotype.Component;

import com.excilys.cdb.crossProject.exceptions.ProblemListException;
import com.excilys.cdb.model.validator.Validator;

public class Page {
    private long offset;
    private long limit;

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

    @Override
    public String toString() {
        return "Page [offset=" + offset + ", limit=" + limit + "]";
    }

    /**
     * Creates builder to build {@link Page}.
     *
     * @return created builder
     */
    public static PageBuilder builder() {
        return new PageBuilder();
    }

    /**
     * Builder to build {@link Page}.
     */
    public static final class PageBuilder {
        private long offset;
        private long limit;

        /**
         * Performs validation before a company is instantiated; should be final
         * but we use BuilderInitializer instead to facilitate DI and testing.
         * Should be effectively final.
         */
        private static Validator<PageBuilder> validator;

        /**
         * Should only be instanciated once by Spring; set the static validator
         * of the builder. Little trick to cope with Spring being unable to
         * autowire static fields
         *
         */
        @Component
        private static class BuilderInitializer {
            private BuilderInitializer(Validator<PageBuilder> validator) {
                PageBuilder.validator = validator;
            }
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

        /**
         * Throws a ProblemListException if the to-be-built page does not pass
         * the validation
         */
        public Page build() throws ProblemListException {
            validator.validate(this);
            return new Page(this);
        }
    }

}
