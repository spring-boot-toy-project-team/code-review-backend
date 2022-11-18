package com.codereview.stub;

import com.codereview.tag.entity.Tag;

public class TagStubData {
  public static Tag tag() {
    return Tag.builder()
      .name("A")
      .tagId(1L)
      .build();
  }
}
