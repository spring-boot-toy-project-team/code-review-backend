package com.codereview.board.repository.board;

import com.codereview.board.document.BoardDocument;
import com.codereview.helper.RestPage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardSearchRepository extends ElasticsearchRepository<BoardDocument, Long> {
  RestPage<BoardDocument> findByKeyword(String keyword, Pageable pageable);
}
