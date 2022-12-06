package com.codereview.stub;

import com.codereview.img.dto.ImgResponseDto;
import com.codereview.img.entity.Img;
import com.codereview.img.entity.ImgType;

public class ImgStubData {
  public static Img img() {
    return Img.builder()
      .imgId(1L)
      .type(ImgType.BOARD)
      .uri("uri")
      .path("path")
      .build();
  }

  public static ImgResponseDto.ImgInfo imgToImgInfo(Img img) {
    return ImgResponseDto.ImgInfo.builder()
      .uri(img.getUri())
      .build();
  }
}
