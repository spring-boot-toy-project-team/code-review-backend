package com.codereview.enrollment.controller;

import com.codereview.enrollment.mapper.EnrollmentMapper;
import com.codereview.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class EnrollmentController {
  private final EnrollmentService enrollmentService;
  private final EnrollmentMapper mapper;

  // TODO: 리뷰 신청
  // TODO: 리뷰 신청 변경
  // TODO: 리뷰 신청 삭제
  // TODO: 내가 요청한 리뷰 신청 조회
  // TODO: 나에게 요청된 리뷰 확인
  // TODO: 나에게 요청된 리뷰 승인
  // TODO: 나에게 요청된 리뷰 거절
}
