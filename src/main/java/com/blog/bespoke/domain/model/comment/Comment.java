package com.blog.bespoke.domain.model.comment;

import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
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

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User currentUser) {
        this.user = currentUser;
    }


    public void update(CommentUpdateCmd cmd) {
        if (cmd.getContent() != null && !cmd.getContent().isBlank()) {
            this.content = cmd.getContent();
        }
    }

}
