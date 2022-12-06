package com.codereview.img.service;

import com.codereview.common.dto.file.FileDto;
import com.codereview.common.exception.BusinessLogicException;
import com.codereview.common.exception.ExceptionCode;
import com.codereview.img.entity.Img;
import com.codereview.img.entity.ImgType;
import com.codereview.img.repository.ImgRepository;
import com.codereview.util.file.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.codereview.util.PathMaker.makePathFromNow;


@Service
@Transactional
@RequiredArgsConstructor
public class ImgService {
  private final ImgRepository imgRepository;
  private final StorageService storageService;

  public Img saveImg(MultipartFile multipartFile, ImgType type) {
    FileDto fileDto = storageService.store(multipartFile, makePathFromNow());
    Img image = Img.builder()
      .uri(fileDto.getUri())
      .path(fileDto.getPath())
      .type(type)
      .build();
    return imgRepository.save(image);
  }

  public void deleteImg(Long imgId) {
    Img findImg = findVerifiedImgById(imgId);
    imgRepository.delete(findImg);
  }

  @Transactional(readOnly = true)
  private Img findVerifiedImgById(Long imgId) {
    Optional<Img> optionalImg = imgRepository.findById(imgId);
    return optionalImg.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMG_NOT_FOUND));
  }
}
