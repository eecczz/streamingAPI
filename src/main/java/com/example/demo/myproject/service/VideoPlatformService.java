package com.example.demo.myproject.service;

import com.example.demo.myproject.dto.api.*;
import com.example.demo.myproject.entity.*;
import com.example.demo.myproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoPlatformService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public List<VideoDTO> listVideos(Long viewerId) {
        User viewer = findViewer(viewerId);
        return memoRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(memo -> toVideoDTO(memo, viewer))
                .toList();
    }

    @Transactional
    public VideoDTO getVideo(Long id, Long viewerId) {
        Memo memo = getMemo(id);
        memo.increaseViewCount();
        User viewer = findViewer(viewerId);
        return toVideoDTO(memo, viewer);
    }

    @Transactional
    public VideoDTO createVideo(VideoCreateRequest request) {
        User uploader = getUser(request.getUploaderId());
        Memo memo = Memo.builder()
                .memoText(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getThumbnailUrl())
                .videoUrl(request.getVideoUrl())
                .hlsUrl(request.getHlsUrl())
                .durationSeconds(request.getDurationSeconds())
                .uploadStatus("READY")
                .visibility("PUBLIC")
                .build();
        memo.setUploader(uploader);
        return toVideoDTO(memoRepository.save(memo), uploader);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> listComments(Long memoId) {
        return commentRepository.findByMemoIdAndParentCommentIdIsNullOrderByCreatedAtAsc(memoId).stream()
                .map(this::toCommentDTO)
                .toList();
    }

    @Transactional
    public CommentDTO addComment(Long memoId, CommentRequest request) {
        Memo memo = getMemo(memoId);
        User commenter = getUser(request.getUserId());
        Comment comment = Comment.builder()
                .memoId(memoId)
                .parentCommentId(request.getParentCommentId())
                .replyText(request.getText())
                .build();
        comment.setCommenter(commenter);
        Comment saved = commentRepository.save(comment);

        if (!memo.getUploader().getId().equals(commenter.getId())) {
            String type = request.getParentCommentId() == null ? "COMMENT" : "REPLY";
            String action = request.getParentCommentId() == null ? "commented on" : "replied to a comment on";
            createNotification(memo.getUploader(), commenter, type, commenter.getChannelName() + " " + action + " your video", memoId, saved.getId());
        }

        return toCommentDTO(saved);
    }

    @Transactional
    public ToggleResponse toggleLike(Long memoId, Long userId) {
        Memo memo = getMemo(memoId);
        User user = getUser(userId);
        return videoLikeRepository.findByMemoAndUser(memo, user)
                .map(existing -> {
                    videoLikeRepository.delete(existing);
                    memo.decreaseLikeCount();
                    return ToggleResponse.builder().active(false).count(memo.getLikeCount()).build();
                })
                .orElseGet(() -> {
                    videoLikeRepository.save(VideoLike.builder().memo(memo).user(user).build());
                    memo.increaseLikeCount();
                    if (!memo.getUploader().getId().equals(user.getId())) {
                        createNotification(memo.getUploader(), user, "LIKE", user.getChannelName() + " liked your video", memoId, null);
                    }
                    return ToggleResponse.builder().active(true).count(memo.getLikeCount()).build();
                });
    }

    @Transactional
    public ToggleResponse toggleSubscription(Long channelOwnerId, Long subscriberId) {
        User channelOwner = getUser(channelOwnerId);
        User subscriber = getUser(subscriberId);
        return subscriptionRepository.findBySubscriberAndChannelOwner(subscriber, channelOwner)
                .map(existing -> {
                    subscriptionRepository.delete(existing);
                    return ToggleResponse.builder()
                            .active(false)
                            .count(subscriptionRepository.countByChannelOwner(channelOwner))
                            .build();
                })
                .orElseGet(() -> {
                    subscriptionRepository.save(Subscription.builder()
                            .subscriber(subscriber)
                            .channelOwner(channelOwner)
                            .build());
                    if (!channelOwner.getId().equals(subscriber.getId())) {
                        createNotification(channelOwner, subscriber, "SUBSCRIBE", subscriber.getChannelName() + " subscribed to your channel", null, null);
                    }
                    return ToggleResponse.builder()
                            .active(true)
                            .count(subscriptionRepository.countByChannelOwner(channelOwner))
                            .build();
                });
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> listLikedVideos(Long userId) {
        User user = getUser(userId);
        return videoLikeRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(like -> toVideoDTO(like.getMemo(), user))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VideoDTO> listUploadedVideos(Long userId) {
        User user = getUser(userId);
        return memoRepository.findByUploaderOrderByCreatedAtDesc(user).stream()
                .map(memo -> toVideoDTO(memo, user))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChannelDTO> listSubscribedChannels(Long userId) {
        User user = getUser(userId);
        return subscriptionRepository.findBySubscriberOrderByCreatedAtDesc(user).stream()
                .map(subscription -> toChannelDTO(subscription.getChannelOwner(), user))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> listNotifications(Long userId) {
        return noteRepository.findByTargetUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toNotificationDTO)
                .toList();
    }

    private void createNotification(User target, User actor, String type, String text, Long memoId, Long commentId) {
        Note note = Note.builder()
                .noteText(text)
                .notingUserName(actor.getChannelName())
                .notificationType(type)
                .sourceMemoId(memoId)
                .sourceCommentId(commentId)
                .build();
        note.setTargetUser(target);
        noteRepository.save(note);
    }

    private Memo getMemo(Long id) {
        return memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found: " + id));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    private User findViewer(Long viewerId) {
        if (viewerId == null) {
            return null;
        }
        return userRepository.findById(viewerId).orElse(null);
    }

    private VideoDTO toVideoDTO(Memo memo, User viewer) {
        boolean liked = viewer != null && videoLikeRepository.existsByMemoAndUser(memo, viewer);
        return VideoDTO.builder()
                .id(memo.getMno())
                .title(memo.getMemoText())
                .description(memo.getDescription())
                .thumbnailUrl(memo.getImageUrl())
                .videoUrl(memo.getVideoUrl())
                .hlsUrl(memo.getHlsUrl())
                .external(false)
                .durationSeconds(memo.getDurationSeconds())
                .viewCount(memo.getViewCount())
                .likeCount(memo.getLikeCount())
                .commentCount(commentRepository.countByMemoId(memo.getMno()))
                .liked(liked)
                .uploadStatus(memo.getUploadStatus())
                .createdAt(memo.getCreatedAt())
                .channel(toChannelDTO(memo.getUploader(), viewer))
                .build();
    }

    private ChannelDTO toChannelDTO(User user, User viewer) {
        boolean subscribed = viewer != null && user != null && subscriptionRepository.existsBySubscriberAndChannelOwner(viewer, user);
        return ChannelDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .channelName(user.getChannelName())
                .avatarUrl(user.getAvatarUrl())
                .description(user.getChannelDescription())
                .subscriberCount(subscriptionRepository.countByChannelOwner(user))
                .subscribed(subscribed)
                .build();
    }

    private CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getReplyText())
                .parentCommentId(comment.getParentCommentId())
                .createdAt(comment.getCreatedAt())
                .likeCount(comment.getLikeCount())
                .author(toChannelDTO(comment.getCommenter(), null))
                .replies(commentRepository.findByParentCommentIdOrderByCreatedAtAsc(comment.getId()).stream()
                        .map(reply -> CommentDTO.builder()
                                .id(reply.getId())
                                .text(reply.getReplyText())
                                .parentCommentId(reply.getParentCommentId())
                                .createdAt(reply.getCreatedAt())
                                .likeCount(reply.getLikeCount())
                                .author(toChannelDTO(reply.getCommenter(), null))
                                .replies(List.of())
                                .build())
                        .toList())
                .build();
    }

    private NotificationDTO toNotificationDTO(Note note) {
        return NotificationDTO.builder()
                .id(note.getId())
                .text(note.getNoteText())
                .type(note.getNotificationType())
                .actorName(note.getNotingUserName())
                .sourceMemoId(note.getSourceMemoId())
                .sourceCommentId(note.getSourceCommentId())
                .read(note.isReadFlag())
                .createdAt(note.getCreatedAt())
                .build();
    }
}
