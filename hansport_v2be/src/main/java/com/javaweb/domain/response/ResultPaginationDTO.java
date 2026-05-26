package com.javaweb.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter
    public static class Meta{
        private int page;
        private int pagesize;
        private int pages;
        private long total;
    }
}
