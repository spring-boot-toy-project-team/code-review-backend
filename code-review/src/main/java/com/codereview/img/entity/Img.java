package com.codereview.img.entity;

import com.codereview.common.audit.Auditable;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

  private String dir;

  @Enumerated(EnumType.STRING)
  private ImgType type;
}