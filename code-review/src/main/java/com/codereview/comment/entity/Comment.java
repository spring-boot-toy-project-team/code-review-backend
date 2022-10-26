package com.codereview.comment.entity;

import com.codereview.board.entity.Board;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public Comment(Long commentId, String contents){
        this.commentId = commentId;
        this.contents = contents;
    }

    public void addBoard(Board board){
        if(this.board == null && board != null){
            this.board = board;
        }
    }
}
