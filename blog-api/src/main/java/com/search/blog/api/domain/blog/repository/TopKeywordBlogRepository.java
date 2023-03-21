package com.search.blog.api.domain.blog.repository;

import com.search.blog.api.domain.blog.cache.domain.TopKeywordBlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopKeywordBlogRepository extends CrudRepository<TopKeywordBlog, String> {


}
