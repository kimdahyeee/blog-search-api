package com.search.blog.api.domain.blog.repository;

import com.search.blog.model.entity.blog.BlogKeyword;
import com.search.blog.model.repository.blog.BaseBlogKeywordRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface BlogKeywordRepository extends BaseBlogKeywordRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "3000")
    })
    Optional<BlogKeyword> findByKeyword(String keyword);

}
