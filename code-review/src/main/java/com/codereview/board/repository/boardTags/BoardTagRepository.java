package com.codereview.board.repository.boardTags;

import com.codereview.board.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardTagRepository extends JpaRepository<BoardTag, Long>, BoardTagRepositoryCustom {

}
