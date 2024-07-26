package com.blog.bespoke.domain.model.comment;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import jakarta.persistence.*;

@Entity
public class Comment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    // 댓글에서 post 의 정보는 필요없다. 유저의 정보는 필요하다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
}
