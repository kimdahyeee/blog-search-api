package com.search.blog.api.core.paging.request;

import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Setter
public class NormalPaging implements Paging {

    private int pageSize;
    private int pageNumber;

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber - 1;
    }

    public Pageable getPageable() {
        return PageRequest.of(getPageNumber(), getPageSize());
    }

}
