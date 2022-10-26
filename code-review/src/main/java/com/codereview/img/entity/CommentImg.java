package com.codereview.img.entity;

import com.codereview.comment.entity.Comment;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class CommentImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentImgId;

    private String dir;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @Builder
    public CommentImg(Long commentImgId, String dir){
        this.commentImgId = commentImgId;
        this.dir = dir;
    }

}
