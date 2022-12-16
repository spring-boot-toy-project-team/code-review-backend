package com.codereview.reviewer.repository;

import com.codereview.reviewer.entity.Reviewer;

import java.util.Optional;

public interface ReviewerRepositoryCustom {
  Optional<Reviewer> findByMemberId(Long memberId);
}
