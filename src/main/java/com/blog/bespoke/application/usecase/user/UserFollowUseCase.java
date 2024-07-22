package com.blog.bespoke.application.usecase.user;

import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.event.message.UserFollowMessage;
import com.blog.bespoke.application.event.message.UserUnFollowMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserRelation;
import com.blog.bespoke.domain.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFollowUseCase {
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    /**
     * check current user followed target user
     */
    @Transactional
    public boolean checkFollow(User currentUser, Long userId) {
        try {
            userRepository.getUserWithFollowByIdAndFollowerId(userId, currentUser.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 팔로우 대상이 return 되는게 정상이다!
    @Transactional
    public UserResponseDto follow(Long followingId, User currentUser) {
        UserRelation relation = UserRelation.builder().count(true).build();

        User followingUser = userRepository.getById(followingId, relation);
        followingUser.addFollower(currentUser.getId());
        UserResponseDto responseDto = UserResponseDto.from(followingUser, relation);
        UserResponseDto.UserCountInfoResponseDto countInfo = responseDto.getCountInfo();
        // 데이터의 정합성이 완전 중요하지 않음. 따라서 현재 기준으로 + 1
        countInfo.setFollowerCount(countInfo.getFollowerCount() + 1);
        eventPublisher.publishFollowEvent(
                UserFollowMessage.builder()
                        .followerId(currentUser.getId())
                        .followerUserNickname(currentUser.getNickname())
                        .followingId(followingId)
                        .followingUserNickname(followingUser.getNickname())
                        .build()
        );
        return responseDto;
    }

    @Transactional
    public UserResponseDto unfollow(Long targetUserId, User currentUser) {
        User targetUser = userRepository.getUserWithFollowByIdAndFollowerId(targetUserId, currentUser.getId());
        targetUser.removeFollower(currentUser.getId());
        UserResponseDto responseDto = UserResponseDto.from(targetUser, UserRelation.builder().count(true).build());
        UserResponseDto.UserCountInfoResponseDto countInfo = responseDto.getCountInfo();
        // 데이터의 정합성이 중요하지 않으므로 현재에서 - 1
        countInfo.setFollowerCount(countInfo.getFollowerCount() - 1);
        eventPublisher.publishUnfollowEvent(UserUnFollowMessage.builder().followerId(currentUser.getId()).followingId(targetUserId).build());
        return responseDto;
    }

}
