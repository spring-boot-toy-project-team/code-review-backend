package com.codereview.board.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;


    @Builder
    public Board(Long boardId, String title, String contents){
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
    }

}
