package com.search.blog.api.external.blog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Setter
@Component
@ConfigurationProperties(prefix = "infra.kakao")
public class KakaoBlogSearchApiConfig extends BlogSearchApiConfig {

    private RestApiKey restApiKey;
    private SearchBlog searchBlog;

    @Setter
    @Getter
    public static class RestApiKey {

        private String id;
        private String key;

    }

    @Setter
    @Getter
    public static class SearchBlog {

        private String url;
        private String uri;

        public String getFullUrl() {
            return this.url + this.uri;
        }

    }

    @Override
    public Consumer<HttpHeaders> getDefaultHeaders() {
        return httpHeaders ->
                httpHeaders.set(HttpHeaders.AUTHORIZATION, this.restApiKey.getId() + " " + this.restApiKey.getKey());
    }

    @Override
    public String getUrl() {
        return this.searchBlog.getUrl();
    }

    @Override
    public String getUri() {
        return this.searchBlog.getUri();
    }
}
