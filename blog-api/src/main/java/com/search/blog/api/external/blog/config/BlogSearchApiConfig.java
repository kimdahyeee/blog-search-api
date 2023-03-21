package com.search.blog.api.external.blog.config;

import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

public abstract class BlogSearchApiConfig {

    public abstract Consumer<HttpHeaders> getDefaultHeaders();

    public abstract String getUrl();

    public abstract String getUri();

}
