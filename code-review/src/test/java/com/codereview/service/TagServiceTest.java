package com.codereview.service;

import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.stub.TagStubData;
import com.codereview.tag.entity.Tag;
import com.codereview.tag.repository.TagRepository;
import com.codereview.tag.service.TagService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
  @Spy
  @InjectMocks
  private TagService tagService;

  @Mock
  private TagRepository tagRepository;

  @Test
  @DisplayName("태그 저장하거나 불러오는 테스트")
  public void findTagOrSaveTest() throws Exception {
    // given
    Tag tag = TagStubData.tag();
    lenient().when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(tag);
    lenient().when(tagRepository.findByName(Mockito.anyString())).thenReturn(Optional.ofNullable(tag));

    // when
    Tag savedTag = tagService.findTagOrSave(tag);

    // then
    Assertions.assertThat(tag).isEqualTo(savedTag);
  }

  @Test
  @DisplayName("태그 찾기 실패 테스트")
  public void findByNameFailTest() throws Exception {
    // given
    String name = "C";

    // when

    // then
    lenient().doThrow(new BusinessLogicException(ExceptionCode.TAG_NOT_FOUND))
      .when(tagService)
      .findTag(name);
  }

  @Test
  @DisplayName("태그 찾기 성공 테스트")
  public void findByNameSuccessTest() throws Exception {
    // given
    Tag tag = TagStubData.tag();
    String name = tag.getName();
    doReturn(tag).when(tagService).findTag(name);

    // when
    Tag findTag = tagService.findTag(name);

    // then
    Assertions.assertThat(tag).isEqualTo(findTag);
  }
}
