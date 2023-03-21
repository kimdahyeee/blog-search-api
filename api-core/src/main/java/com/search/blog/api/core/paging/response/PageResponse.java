package com.search.blog.api.core.paging.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class PageResponse<T> {

    private int pageNumber;
    private long totalCount;
    private List<T> datas;

    private PageResponse(int pageNumber, long totalCount, List<T> datas) {
        this.pageNumber = pageNumber;
        this.totalCount = totalCount;
        this.datas = datas;
    }

    public static <T> PageResponse of(int pageNumber, long totalCount, List<T> datas) {
        return new PageResponse(pageNumber, totalCount, datas);
    }

}
