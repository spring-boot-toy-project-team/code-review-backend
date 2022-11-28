package com.codereview.board.repository.board;

import com.codereview.board.document.BoardDocument;
import com.codereview.helper.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BoardSearchRepositoryImpl implements BoardSearchRepository {

  @Override
  public RestPage<BoardDocument> findByKeyword(String keyword) {
    return null;
  }

  @Override
  public Page<BoardDocument> searchSimilar(BoardDocument entity, String[] fields, Pageable pageable) {
    return null;
  }
}
