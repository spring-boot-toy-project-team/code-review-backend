package com.codereview.reviewer.repository;

import com.codereview.reviewer.entity.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Long>, ReviewerRepositoryCustom {

}
