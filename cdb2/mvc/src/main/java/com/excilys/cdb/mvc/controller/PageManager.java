package com.excilys.cdb.mvc.controller;

public class PageManager {
    private long limit;
    private long offset;
    private long nbItem;

    private PageManager(Builder builder) {
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.nbItem = builder.nbItem;
    }

    public long getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }

    public long getNbItem() {
        return nbItem;
    }

    public long nbPages() {
        return nbItem / limit + ((nbItem % limit == 0) ? 0 : 1);
    }

    public long lastPageOffset() {
        if (nbPages() <= 1) {
            return 0;
        } else {
            return limit * (nbPages() - 1);
        }
    }

    public long nextOffset() {
        if (offset + limit >= nbItem) {
            return offset;
        } else {
            return offset + limit;
        }
    }

    public long previousOffset() {
        if (offset - limit < 0) {
            return offset;
        } else {
            return offset - limit;
        }
    }

    private PageManager() {
    }

    /**
     * Creates builder to build {@link PageManager}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link PageManager}.
     */
    public static final class Builder {
        private long limit = 10;
        private long offset = 0;
        private long nbItem = 0;

        private Builder() {
        }

        public Builder withLimit(long limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public Builder withNbItem(long nbItem) {
            this.nbItem = nbItem;
            return this;
        }

        public PageManager build() {
            return new PageManager(this);
        }
    }

}
