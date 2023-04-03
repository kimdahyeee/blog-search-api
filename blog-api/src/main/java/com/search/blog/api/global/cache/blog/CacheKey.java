package com.search.blog.api.global.cache.blog;

public interface CacheKey {

    /**
     * keywords 의 top 10 을 구하기 위한 sortedSet
     */
    String KEYWORDS_SET = "keywords";

    String TOP_KEYWORDS = "topKeywords";
    String TOP_KEYWORDS_RESULTS = "topKeywordsBlogs";

}
