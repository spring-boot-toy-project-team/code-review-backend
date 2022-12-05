package com.codereview.img.entity;

import com.codereview.common.audit.Auditable;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Img extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long imgId;

  private String uri;

  private String path;

  @Enumerated(EnumType.STRING)
  private ImgType type;
}
