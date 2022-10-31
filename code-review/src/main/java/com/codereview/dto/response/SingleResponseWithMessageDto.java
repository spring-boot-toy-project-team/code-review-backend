package com.codereview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleResponseWithMessageDto<T>{
  private T data;
  private String message;
}