package com.codereview.img.controller;

import com.codereview.common.dto.response.SingleResponseWithMessageDto;
import com.codereview.img.entity.Img;
import com.codereview.img.entity.ImgType;
import com.codereview.img.mapper.ImgMapper;
import com.codereview.img.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImgController {
  private final ImgService imgService;
  private final ImgMapper mapper;

  @PostMapping(value = "/{type}",  consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<SingleResponseWithMessageDto> uploadImg(@RequestPart(name = "file", required = true) MultipartFile multipartFile,
                                                                @PathVariable("type") String type) {
    Img img = imgService.saveImg(multipartFile, ImgType.valueOf(type.toUpperCase()));
    return new ResponseEntity<>(new SingleResponseWithMessageDto(mapper.imgToImgInfo(img),
      "SUCCESS"),
      HttpStatus.OK);
  }
}
