package com.codereview.enrollment.service;

import com.codereview.enrollment.entity.Enrollment;
import com.codereview.enrollment.repository.EnrollmentRepository;
import com.codereview.util.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;
  private final CustomBeanUtils<Enrollment> beanUtils;

}
