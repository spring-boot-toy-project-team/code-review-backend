package com.codereview.tag.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.tag.entity.Tag;
import com.codereview.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {
  private final TagRepository tagRepository;

  public Tag findTagOrSave(Tag tag) {
    Optional<Tag> optionalTag = tagRepository.findByName(tag.getName());
    return optionalTag.orElseGet(() -> tagRepository.saveAndFlush(tag));
  }

  @Transactional(readOnly = true)
  public Tag findTag(String name) {
    Optional<Tag> optionalTag = tagRepository.findByName(name);
    return optionalTag.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TAG_NOT_FOUND));
  }
}
