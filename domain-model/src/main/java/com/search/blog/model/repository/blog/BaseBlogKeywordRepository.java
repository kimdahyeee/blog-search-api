package com.search.blog.model.repository.blog;

import com.search.blog.model.entity.blog.BlogKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseBlogKeywordRepository extends JpaRepository<BlogKeyword, Long> {

}
