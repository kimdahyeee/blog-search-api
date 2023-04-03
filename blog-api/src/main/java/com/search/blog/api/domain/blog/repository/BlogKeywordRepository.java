package com.search.blog.api.domain.blog.repository;

import com.search.blog.model.entity.blog.BlogKeyword;
import com.search.blog.model.repository.blog.BaseBlogKeywordRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface BlogKeywordRepository extends BaseBlogKeywordRepository {

    /**
     * 동시성 이슈 고려 (1)
     *
     * Lock 어노테이션도 aop 를 통해 동작한다.
     * - default lockModeType 이 PESSIMISTIC_WRITE 이다.
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "3000")
    })
    Optional<BlogKeyword> findByKeyword(String keyword);

}
