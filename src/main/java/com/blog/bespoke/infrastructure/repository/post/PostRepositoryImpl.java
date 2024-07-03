package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.repository.post.PostRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
        postJpaRepository.incrementViewCount(postId);
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
    public Page<Post> search(PostSearchCond cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getPageSize());
        JPAQuery<Post> jpaQuery = query(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        applyOrderBy(jpaQuery, cond.getOrderBy());
        List<Post> posts = jpaQuery.fetch();

        Long totalSize = countQuery(cond).fetch().get(0);

        return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);
    }

    // TODO: 이렇게 조인해오면 author 의 연관관계 중 eager 를 가져옴. 근데 난 그거 싫은데..
    private JPAQuery<Post> query(PostSearchCond cond) {
        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .leftJoin(post.author).fetchJoin()
                .leftJoin(post.postCountInfo).fetchJoin();

        applyLike(query, cond);
        applyFollow(query, cond);

        return query
                .where(
                        statusEq(cond),
                        authorIdEq(cond)
                );
    }

    private JPAQuery<Long> countQuery(PostSearchCond cond) {
        JPAQuery<Long> query = queryFactory.select(Wildcard.count)
                .from(post);

        applyLike(query, cond);
        applyFollow(query, cond);

        return query
                .where(
                        statusEq(cond),
                        authorIdEq(cond)
                );
    }

    /**
     * - 좋아요순
     * - 최신순. 기본으로 최신순
     * - 조회순
     * - 댓글순
     */
    private void applyOrderBy(JPAQuery<Post> query, PostSearchCond.OrderBy orderBy) {
        if (orderBy.equals(PostSearchCond.OrderBy.LATEST)) {
            query.orderBy(post.createdAt.desc());
        } else if (orderBy.equals(PostSearchCond.OrderBy.COMMENT)) {
            log.info("comment 수 를 이용한 정렬 미구현.");
            query.orderBy(post.postCountInfo.commentCount.desc());
        } else if (orderBy.equals(PostSearchCond.OrderBy.LIKE)) {
            query.orderBy(post.postCountInfo.likeCount.desc());
        } else if (orderBy.equals(PostSearchCond.OrderBy.VIEW)) {
            query.orderBy(post.postCountInfo.viewCount.desc());
        }
        query.orderBy(post.createdAt.desc());
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

    private BooleanExpression authorIdEq(PostSearchCond cond) {
        if (cond == null || cond.getAuthorId() == null) {
            return null;
        }
        return post.author.id.eq(cond.getAuthorId());
    }

    private BooleanExpression statusEq(PostSearchCond cond) {
        if (cond == null || cond.getStatus() == null) {
            return post.status.eq(Post.Status.PUBLISHED);
        }
        return post.status.eq(cond.getStatus());
    }
}
