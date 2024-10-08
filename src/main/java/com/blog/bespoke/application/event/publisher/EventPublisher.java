package com.blog.bespoke.application.event.publisher;

import com.blog.bespoke.application.event.message.*;

public interface EventPublisher {
    // 이메일 관련 이벤트(공통화)
    void publishMailEvent(UserRegistrationMessage message);

    // NOTE: 팔로우 시 발생
    void publishFollowEvent(UserFollowMessage message);

    // NOTE: 언팔로우 시 발생
    void publishUnfollowEvent(UserUnFollowMessage message);

    // NOTE: create 가 아닌 published 이벤트로 변경해야함
    void publishPostPublishEvent(PublishPostEvent message);

    // NOTE:
    void publishPostLikeEvent(PostLikeMessage message);

    void publishPostCancelLikeEvent(PostLikeCancelMessage message);

    // NOTE: 댓글 추가 시 발생
    void publishCommentAddEvent(CommentAddMessage message);

    // NOTE: 댓글 삭제 시 발생
    void publishCommentDeleteEvent(CommentDeleteMessage message);

    void publishBannerAuditApproveEvent(BannerAuditApproveMessage message);

    void publishBannerAuditDenyEvent(BannerAuditDenyMessage message);
}
