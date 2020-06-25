package com.excilys.cdb.servlet;

import javax.annotation.Generated;

public class Order {
    public enum OrderBy {
        COMPUTER, COMPANY, INTRODUCED, DISCONTINUED
    };

    private final boolean asc;
    private final OrderBy orderBy;

    @Generated("SparkTools")
    private Order(Builder builder) {
        this.asc = builder.asc;
        this.orderBy = builder.orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    /**
     * Creates builder to build {@link Order}.
     *
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link Order}.
     */
    @Generated("SparkTools")
    public static final class Builder {
        private boolean asc;
        private OrderBy orderBy;

        private Builder() {
        }

        public Builder withAsc(boolean asc) {
            this.asc = asc;
            return this;
        }

        public Builder withOrderBy(OrderBy orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

}
