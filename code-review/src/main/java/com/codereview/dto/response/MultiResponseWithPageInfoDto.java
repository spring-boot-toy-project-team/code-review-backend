package com.codereview.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MultiResponseWithPageInfoDto<T>{
  private List<T> data;
  private PageInfo pageInfo;

  public MultiResponseWithPageInfoDto(List<T> data, Page page) {
    this.data = data;
    this.pageInfo = new PageInfo(page.getNumber() + 1,
      page.getSize(), page.getTotalElements(), page.getTotalPages());
  }
}