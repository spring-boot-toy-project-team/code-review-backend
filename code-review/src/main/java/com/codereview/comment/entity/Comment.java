package com.codereview.comment.entity;

import com.codereview.board.entity.Board;
import com.codereview.common.audit.Auditable;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public Comment(Long commentId, String contents){
        this.commentId = commentId;
        this.contents = contents;
    }
}
