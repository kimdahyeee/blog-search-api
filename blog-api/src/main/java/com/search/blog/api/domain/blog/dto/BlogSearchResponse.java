package com.search.blog.api.domain.blog.dto;

import com.search.blog.api.core.paging.response.PageResponse;
import com.search.blog.api.external.blog.response.KakaoBlogSearchApiItem;
import com.search.blog.api.external.blog.response.KakaoBlogSearchApiResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchResponse implements Serializable {

    private static final long serialVersionUID = 835613172520787168L;

    PageResponse<BlogResponse> blogResponses;

    public BlogSearchResponse(PageResponse<BlogResponse> blogResponses) {
        this.blogResponses = blogResponses;
    }

    public static BlogSearchResponse of(Integer pageNumber, KakaoBlogSearchApiResponse kakaoRepsonses) {
        List<BlogResponse> datas = kakaoRepsonses.getDocuments().stream()
                .map(BlogResponse::new)
                .collect(Collectors.toList());

        return new BlogSearchResponse(PageResponse.of(pageNumber, kakaoRepsonses.getTotalCount(), datas));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BlogResponse implements Serializable {


        private static final long serialVersionUID = 590795777149963016L;
        private String title;
        private String contents;
        private String url;
        private String blogName;
        private String thumbnail;

        private LocalDateTime dateTime;

        public BlogResponse(final KakaoBlogSearchApiItem kakaoBlogSearchApiItem) {
            this.title = kakaoBlogSearchApiItem.getTitle();
            this.contents = kakaoBlogSearchApiItem.getContents();
            this.url = kakaoBlogSearchApiItem.getUrl();
            this.blogName = kakaoBlogSearchApiItem.getBlogname();
            this.thumbnail = kakaoBlogSearchApiItem.getThumbnail();
            this.dateTime = kakaoBlogSearchApiItem.getDatetime();
        }

    }
}
