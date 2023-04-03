package com.search.blog.api.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkResponse<T> {

    private String message;
    /**
     * data parameter 로 한번 더 감싸주는 이유는, 데이터가 있건 없건 okResponse 의 전체적인 구조를 깨지 않기 위함
     * -> 클라이언트에서 일관적인 응답 처리를 할 수 있는 장점 !
     */
    private T data;
    @Deprecated
    private PageInfo page;

    private OkResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    private OkResponse(String message, T data, PageInfo page) {
        this.message = message;
        this.data = data;
        this.page = page;
    }

    public static <T> OkResponse<T> of(T data) {
        return new OkResponse<>(null, data);
    }

    public static <T> OkResponse<T> of(String message, T data) {
        return new OkResponse<>(message, data);
    }

    public static <T> OkResponse<T> paging(T data, PageInfo page) {
        return new OkResponse<>(null, data, page);
    }

    @Getter
    public static class PageInfo {

        private int pageNumber;
        private long totalCount;

        public PageInfo(int pageNumber, long totalCount) {
            this.pageNumber = pageNumber;
            this.totalCount = totalCount;
        }

        public static PageInfo of(int pageNumber, long totalCount) {
            return new PageInfo(pageNumber, totalCount);
        }
    }

}
