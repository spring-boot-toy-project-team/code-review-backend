package com.codereview.tag.entity;

import com.codereview.board.entity.Board;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class BoardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardTagId;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "TAG_ID")
    private Tag tag;

    @Builder
    public BoardTag(Long boardTagId){
        this.boardTagId = boardTagId;
    }

    public void addBoard(Board board){
        if(this.board == null && board != null){
            this.board = board;
        }
    }

    public void addTag(Tag tag){
        if(this.tag == null && tag != null){
            this.tag = tag;
        }
    }
}
