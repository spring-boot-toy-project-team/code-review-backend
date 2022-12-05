package com.codereview.img.service;

import com.codereview.img.entity.Image;
import com.codereview.img.repository.ImageRepository;
import com.codereview.util.file.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;
  private final StorageService storageService;

  public Image saveImage(MultipartFile multipartFile, String destination) {
//    Date date = new Date();
//    date.toString()
    String path = "";
    Image image = Image.builder()
      .dir(storageService.store(multipartFile, path))
      .build();
    return imageRepository.save(image);
  }


}
