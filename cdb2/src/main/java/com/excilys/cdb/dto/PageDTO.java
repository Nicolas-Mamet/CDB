package com.excilys.cdb.dto;

public class PageDTO {
    private String offset;
    private String limit;

    private PageDTO(Builder builder) {
        this.offset = builder.offset;
        this.limit = builder.limit;
    }

    public String getOffset() {
        return offset;
    }

    public String getLimit() {
        return limit;
    }

    /**
     * Creates builder to build {@link PageDTO}.
     * 
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link PageDTO}.
     */
    public static final class Builder {
        private String offset;
        private String limit;

        private Builder() {
        }

        public Builder withOffset(String offset) {
            this.offset = offset;
            return this;
        }

        public Builder withLimit(String limit) {
            this.limit = limit;
            return this;
        }

        public PageDTO build() {
            return new PageDTO(this);
        }
    }
}
