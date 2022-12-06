package com.codereview.img.mapper;

import com.codereview.img.dto.ImgResponseDto;
import com.codereview.img.entity.Img;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImgMapper {
  ImgResponseDto.ImgInfo imgToImgInfo(Img img);
}
