package com.codereview.service;

import com.codereview.common.dto.file.FileDto;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.img.entity.Img;
import com.codereview.img.entity.ImgType;
import com.codereview.img.repository.ImgRepository;
import com.codereview.img.service.ImgService;
import com.codereview.stub.ImgStubData;
import com.codereview.util.file.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.codereview.util.PathMaker.makePathFromNow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImgServiceTest {
  @Spy
  @InjectMocks
  private ImgService imgService;

  @Mock
  private ImgRepository imgRepository;

  @Mock
  private StorageService storageService;

  @Nested
  @DisplayName("이미지 저장 테스트")
  public class saveImgTest {
    @Test
    @DisplayName("성공")
    public void saveImgSuccessTest() throws Exception {
      // given
      MultipartFile multipartFile = new MockMultipartFile("test.jpg", new byte[]{'t'});
      FileDto fileDto = FileDto.builder()
        .path(makePathFromNow()+"/test.jpg")
        .uri(makePathFromNow()+"/test.jpg")
        .build();
      Img image = Img.builder()
        .uri(fileDto.getUri())
        .path(fileDto.getPath())
        .type(ImgType.BOARD)
        .build();
      given(imgRepository.save(Mockito.any(Img.class))).willReturn(image);
      doReturn(fileDto).when(storageService).store(multipartFile, makePathFromNow());

      // when
      Img savedImg = imgService.saveImg(multipartFile, ImgType.BOARD);

      // then
      Assertions.assertThat(image).isEqualTo(savedImg);
    }

    @Test
    @DisplayName("실패 - 파일 확장자 이미지 아닐 경우")
    public void saveImgFailFileIsNotImageTest() throws Exception {
      // given
      MultipartFile multipartFile = new MockMultipartFile("test.mp4", new byte[]{'t'});

      doThrow(new BusinessLogicException(ExceptionCode.INVALID_FILE_FORMAT))
        .when(storageService).store(multipartFile, makePathFromNow());

      // when

      // then
      Assertions.assertThatThrownBy(() -> imgService.saveImg(multipartFile, ImgType.BOARD))
        .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("실패 - 파일 없을 경우")
    public void saveImgFailFileIsNullTest() throws Exception {
      // given
      doThrow(new BusinessLogicException(ExceptionCode.INVALID_FILE_FORMAT))
        .when(storageService).store(null, makePathFromNow());

      // when

      // then
      Assertions.assertThatThrownBy(() -> imgService.saveImg(null, ImgType.BOARD))
        .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    @DisplayName("실패 - 게시글/댓글의 타입이 아닐 경우")
    public void saveImgFailInvalidTypeTest() throws Exception {
      // given
      MultipartFile multipartFile = new MockMultipartFile("test.jpg", new byte[]{'t'});

      // when

      // then
      Assertions.assertThatThrownBy(() -> imgService.saveImg(multipartFile, ImgType.valueOf("undefined")))
        .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("이미지 식별자로 삭제 테스트")
  public class deleteImgTest {
    @Test
    @DisplayName("성공")
    public void deleteImgSuccessTest() throws Exception {
      // given
      Img savedImg = ImgStubData.img();
      Long imgId = 1L;
      given(imgRepository.findById(Mockito.anyLong())).willReturn(Optional.ofNullable(savedImg));

      // when
      imgService.deleteImg(imgId);

      // then
      verify(imgRepository).delete(argThat(img -> img.getImgId().equals(imgId)));
    }

    @Test
    @DisplayName("삭제")
    public void deleteImgFailTest() throws Exception {
      // given
      Long imgId = 2L;
      given(imgRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
      // when

      // then
      Assertions.assertThatThrownBy(() -> imgService.deleteImg(imgId))
        .isInstanceOf(BusinessLogicException.class);
    }
  }
}
