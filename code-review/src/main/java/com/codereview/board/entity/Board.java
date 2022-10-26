package com.codereview.board.entity;

import com.codereview.comment.entity.Comment;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    private String contents;


    @Builder
    public Board(Long boardId, String title, String contents){
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
    }

}
