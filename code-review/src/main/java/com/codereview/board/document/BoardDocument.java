package com.codereview.board.document;

import com.codereview.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;
import java.time.LocalDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "board")
@Mapping(mappingPath = "elastic/board-mapping.json")
@Setting(settingPath = "elastic/board-setting.json")
public class BoardDocument {
  @Id
  private Long boardId;

  private String title;

  private String contents;

  @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
  private LocalDateTime createdAt;

  @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
  private LocalDateTime modifiedAt;

  public static BoardDocument from(Board board) {
    return BoardDocument.builder()
      .boardId(board.getBoardId())
      .createdAt(board.getCreatedAt())
      .modifiedAt(board.getModifiedAt())
      .title(board.getTitle())
      .contents(board.getContents())
      .build();
  }
}
