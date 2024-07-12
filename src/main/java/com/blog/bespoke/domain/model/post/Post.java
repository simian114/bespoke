package com.blog.bespoke.domain.model.post;

import com.blog.bespoke.domain.model.category.Category;
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

import java.util.HashSet;
import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> postLikes;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostCountInfo postCountInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_image_id")
    private S3PostImage coverImage;

    // TODO: s3 images
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<S3PostImage> images;

    // --- transient
    @Transient
    private Boolean likedByUser = false;

    public void init(User author) {
        this.author = author;
        this.status = Status.DRAFT;
        this.postCountInfo = PostCountInfo.builder().post(this).build();
    }

    // --- domain logic
    @Transient
    public boolean isPublished() {
        return status == Status.PUBLISHED;
    }

    public void update(PostUpdateCmd postUpdateCmd) {
        if (postUpdateCmd.getTitle() != null && !postUpdateCmd.getTitle().isBlank()) {
            title = postUpdateCmd.getTitle();
        }
        if (postUpdateCmd.getContent() != null) {
            content = postUpdateCmd.getContent();
        }
        if (postUpdateCmd.getDescription() != null) {
            description = postUpdateCmd.getDescription();
        }
        if (postUpdateCmd.getCategory() != null) {
            category = postUpdateCmd.getCategory();
        }
    }

    public boolean canUpdateBy(User currentUser) {
        return currentUser.isAdmin() || (Objects.equals(author.getId(), currentUser.getId()));
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public boolean isBlocked() {
        return status == Status.BLOCKED;
    }

    public void addLike(User currentUser) {
        if (postLikes == null) {
            postLikes = new HashSet<>();
        }
        postLikes.add(PostLike.builder().post(this).user(currentUser).build());

    }

    public void cancelLike(Long postId, User currentUser) {
        if (postLikes == null) {
            return;
        }
        postLikes.stream()
                .filter(like -> like.getPost().id.equals(postId) && like.getUser().getId().equals(currentUser.getId()))
                .findFirst()
                .ifPresent(like -> postLikes.remove(like));

    }

    public void setLikedByUser(boolean b) {
        likedByUser = b;
    }

    public void setCategory(Category category) {
        if (category == null) {
            return;
        }
        this.category = category;
    }


    public enum Status {
        DRAFT,
        PUBLISHED,
        HIDDEN, // 숨김처리
        BLOCKED
    }
}
