package com.codereview.board.repository.board;

import com.codereview.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
  Page<Board> findAllByMemberId(long memberId, Pageable pageable);
}
