package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.comment.Comment;
import com.blog.bespoke.domain.model.common.TimeStamp;
import com.blog.bespoke.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Post extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String description;
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    // TODO: category, tags
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    // TODO: s3 images
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> postLikes;

    public void setAuthor(User author) {
        this.author = author;
    }

    // --- domain logic
    @Transient
    public boolean isPublished() {
        return status == Status.PUBLISHED;
    }

    public enum Status {
        DRAFT,
        PUBLISHED,
        BLOCKED
    }
}
