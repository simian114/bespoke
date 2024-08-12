package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.category.QCategory;
import com.blog.bespoke.domain.model.comment.QComment;
import com.blog.bespoke.domain.model.post.*;
import com.blog.bespoke.domain.model.user.QUser;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.post.PostRepository;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.blog.bespoke.domain.model.follow.QFollow.follow;
import static com.blog.bespoke.domain.model.post.QPost.post;
import static com.blog.bespoke.domain.model.post.QPostLike.postLike;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JPAQueryFactory queryFactory;
    private final PostJpaRepository postJpaRepository;
    private final EntityManager em;

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public Post getById(Long id) throws RuntimeException {
        return postJpaRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.POST_NOT_FOUND)
        );
    }


    @Override
    public void delete(Post post) {
        postJpaRepository.delete(post);
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsPostLikeByPostIdAndUserId(Long postId, Long userId) {
        return postJpaRepository.existsPostLikeByPostIdAndUserId(postId, userId);
    }

    @Override
    public Optional<Post> findPostWithLikeByPostIdAndUserId(Long postId, Long userId) {
        return postJpaRepository.findPostWithLikeByIdAndUserId(postId, userId);
    }

    @Override
    public Post getPostWithLikeByPostIdAndUserId(Long postId, Long userId) {
        return findPostWithLikeByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_LIKE_NOT_FOUND));
    }

    @Override
    public void incrementLikeCount(Long postId) {
        postJpaRepository.incrementLikeCount(postId);
    }

    @Override
    public void decrementLikeCount(Long postId) {
        postJpaRepository.decrementLikeCount(postId);

    }

    @Override
    public void incrementViewCount(Long postId) {
        em.createQuery("""
                        UPDATE PostCountInfo p
                        SET p.viewCount = p.viewCount + 1
                        WHERE p.postId = :postId
                        """
                ).setParameter("postId", postId)
                .executeUpdate();
        em.flush();
    }

    @Override
    public void incrementCommentCount(Long postId) {
        postJpaRepository.incrementCommentCount(postId);
    }

    @Override
    public void decrementCommentCount(Long postId) {
        postJpaRepository.decrementCommentCount(postId);
    }

    // --- search
    @Override
    public Optional<Post> findById(Long postId, PostRelation relation) {

        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .distinct()
                .where(post.id.eq(postId));

        QCategory category = QCategory.category;
        QPostCountInfo postCountInfo = QPostCountInfo.postCountInfo;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        if (relation.isAuthor()) {
            query.leftJoin(post.author, user).fetchJoin();
        }
        if (relation.isCategory()) {
            query.leftJoin(post.category, category).fetchJoin();
        }
        if (relation.isCount()) {
            query.leftJoin(post.postCountInfo, postCountInfo).fetchJoin();
        }
        if (relation.isComments()) {
            query.leftJoin(post.comments, comment).fetchJoin()
                    .leftJoin(comment.user, user).fetchJoin();
        }
        if (relation.isCover()) {
            query.leftJoin(post.cover, QS3PostImage.s3PostImage).fetchJoin();
        }
        if (relation.isImages()) {
            query.leftJoin(post.images, QS3PostImage.s3PostImage).fetchJoin();
        }
        try {
            return Optional.ofNullable(query.fetchOne());
        } catch (NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Post getById(Long id, PostRelation relation) throws RuntimeException {
        return findById(id, relation)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    @Override
    public Page<Post> search(PostSearchCond cond) {
        Pageable pageable = PageRequest.of(cond.getPage() == null ? 0 : cond.getPage(), cond.getPageSize() == null ? 20 : cond.getPageSize());

        // query(post, cond)
        // count = query(WildCard.ALL, cond).fetch().get(0) // left join 제외하고
        JPAQuery<Post> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifier(cond.getOrderBy()), post.id.desc());

        List<Post> posts = jpaQuery.fetch();

        Long totalSize = countQuery(cond).fetch().get(0);

        return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);
    }


    // TODO: 이렇게 조인해오면 author 의 연관관계 중 eager 를 가져옴. 근데 난 그거 싫은데..
    private JPAQuery<Post> query(PostSearchCond cond) {
        JPAQuery<Post> query = queryFactory
                .select(Projections.fields(
                                Post.class,
                                getFields(cond).toArray(new Expression<?>[0])
                        )
                )
                .from(post)
                .leftJoin(post.author)
                .leftJoin(post.author.avatar)
                .leftJoin(post.cover)
                .leftJoin(post.postCountInfo);
        applyLike(query, cond);
        applyFollow(query, cond);

        return query
                .where(
                        statusEq(cond),
                        authorIdEq(cond),
                        nicknameEq(cond),
                        categoryEq(cond),
                        filterDeletePost(),
                        titleContain(cond),
                        from(cond),
                        to(cond),
                        statusesIn(cond)
                );
    }

    private List<Expression<?>> getFields(PostSearchCond cond) {
        List<Expression<?>> selectedFields = new ArrayList<>(Arrays.asList(
                post.id,
                post.title,
                post.description,
                post.content,
                post.status,
                post.createdAt,
                post.updatedAt,
                Projections.fields(
                        PostCountInfo.class,
                        post.postCountInfo.viewCount,
                        post.postCountInfo.likeCount,
                        post.postCountInfo.commentCount
                ).as("postCountInfo"),
                Projections.fields(
                        User.class,
                        post.author.id,
                        post.author.email,
                        post.author.nickname,
                        post.author.name,
                        post.author.createdAt,
                        post.author.avatar
                ).as("author"),
                // TODO: 이게 들어가면 list 에서 불러올 때 cover 가 없는건 불러오질 않음.
                Projections.fields(
                        S3PostImage.class,
                        post.cover.id,
                        post.cover.type,
                        post.cover.url,
                        post.cover.originalFilename,
                        post.cover.filename,
                        post.cover.size,
                        post.cover.mimeType
                ).as("cover")
        ));
        if (cond.getUserId() > 0) {
            selectedFields.add(
                    JPAExpressions.selectOne()
                            .from(postLike)
                            .where(postLike.post.eq(post).and(postLike.user.id.eq(cond.getUserId())))
                            .exists()
                            .as("likedByUser")
            );

        }
        return selectedFields;
    }

    private JPAQuery<Long> countQuery(PostSearchCond cond) {
        JPAQuery<Long> query = queryFactory.select(Wildcard.count)
                .from(post);

        applyLike(query, cond);
        applyFollow(query, cond);

        return query
                .where(
                        statusEq(cond),
                        authorIdEq(cond),
                        nicknameEq(cond),
                        categoryEq(cond),
                        filterDeletePost(),
                        titleContain(cond),
                        from(cond),
                        to(cond),
                        statusesIn(cond)
                );
    }

    /**
     * - 최신순. 기본으로 최신순
     * - 좋아요순
     * - 조회순
     * - 댓글순
     */
    private OrderSpecifier<?> createOrderSpecifier(PostSearchCond.OrderBy orderBy) {
        return switch (orderBy) {
            case LATEST -> post.createdAt.desc();
            case COMMENT -> post.postCountInfo.commentCount.desc();
            case LIKE -> post.postCountInfo.likeCount.desc();
            case VIEW -> post.postCountInfo.viewCount.desc();
        };
    }

    /**
     * 삭제 된 게시글은 걸러서
     */
    private BooleanExpression filterDeletePost() {
        return post.deletedAt.isNull();
    }


    /**
     * 내가 좋아요 한 게시글 리스트
     */
    private <T> void applyLike(JPAQuery<T> query, PostSearchCond cond) {
        if (Boolean.TRUE.equals(cond.getLike()) && cond.getUserId() > 0) {
            query.leftJoin(post.postLikes, postLike)
                    .where(postLike.user.id.eq(cond.getUserId()));
        }
    }

    /**
     * 내가 팔로우 한 유저들의 게시글 리스트
     * 1. 팔로우 한 유저의 id 리스트를 먼저 불러온다.
     * 2. post search 쿼리의 where 문에 1번에서 구한 팔로우 한 유저의 id 리스트를 넣는다.
     */
    private <T> void applyFollow(JPAQuery<T> query, PostSearchCond cond) {
        if (Boolean.TRUE.equals(cond.getFollow()) && cond.getUserId() > 0) {
            List<Long> followingIds = queryFactory.select(follow.followingId)
                    .from(follow)
                    .where(follow.followerId.eq(cond.getUserId()))
                    .fetch();
            query.where(post.author.id.in(followingIds));
        }
    }


    /**
     * nickname 과 authorId 가 둘 다 존재하면 안됨. 둘 중 하나만 존재해야한다. PostSearchCond 에 로직 작성할 것.
     */
    private BooleanExpression authorIdEq(PostSearchCond cond) {
        if (cond == null || cond.getAuthorId() == null) {
            return null;
        }
        return post.author.id.eq(cond.getAuthorId());
    }

    private BooleanExpression nicknameEq(PostSearchCond cond) {
        if (cond == null || cond.getNickname() == null || cond.getNickname().isEmpty()) {
            return null;
        }
        return post.author.nickname.eq(cond.getNickname());
    }

    private BooleanExpression categoryEq(PostSearchCond cond) {
        if (cond == null || cond.getCategory() == null) {
            return null;
        }
        return post.category.id.eq(cond.getCategory());
    }

    /**
     * manage 가 false 인 경우 status 의 기본값은 PUBLISHED 로 세팅된다.
     * manage 가 true 인 경우 status 조건은 따지지 않는다.
     */
    private BooleanExpression statusEq(PostSearchCond cond) {
        if (cond != null && cond.getManage() != null && cond.getManage() && cond.getStatus() == null) {
            return null;
        }
        if (cond != null && cond.getStatuses() != null) {
            return null;
        }
        if (cond == null || cond.getStatus() == null) {
            return post.status.eq(Post.Status.PUBLISHED);
        }
        return post.status.eq(cond.getStatus());
    }


    private BooleanExpression titleContain(PostSearchCond cond) {
        if (cond == null || cond.getTitle() == null || cond.getTitle().isEmpty()) {
            return null;
        }
        return post.title.containsIgnoreCase(cond.getTitle());
    }

    private BooleanExpression from(PostSearchCond cond) {
        if (cond == null || cond.getFrom() == null) {
            return null;
        }
        return post.createdAt.after(cond.getFrom());
    }

    private BooleanExpression to(PostSearchCond cond) {
        if (cond == null || cond.getTo() == null) {
            return null;
        }
        return post.createdAt.before(cond.getTo());
    }

    /**
     * statuses 가 빈 리스트면 모든 상태
     */
    private BooleanExpression statusesIn(PostSearchCond cond) {
        if (cond.getStatuses() != null && cond.getStatuses().isEmpty()) {
            return null;
        }
        if (cond == null || cond.getStatuses() == null) {
            return null;
       }
        return post.status.in(cond.getStatuses());
    }
}
