package com.example.demo.myproject.config;

import com.example.demo.myproject.entity.Comment;
import com.example.demo.myproject.entity.Memo;
import com.example.demo.myproject.entity.Subscription;
import com.example.demo.myproject.entity.User;
import com.example.demo.myproject.entity.VideoLike;
import com.example.demo.myproject.repository.CommentRepository;
import com.example.demo.myproject.repository.MemoRepository;
import com.example.demo.myproject.repository.SubscriptionRepository;
import com.example.demo.myproject.repository.UserRepository;
import com.example.demo.myproject.repository.VideoLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DemoDataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;
    private final CommentRepository commentRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User creator = userRepository.save(User.builder()
                .userName("creator")
                .email("creator@example.com")
                .channelName("seondev")
                .avatarUrl("https://api.dicebear.com/8.x/initials/svg?seed=seondev")
                .channelDescription("Large-file upload, encoding, and streaming experiments.")
                .build());
        User viewer = userRepository.save(User.builder()
                .userName("viewer")
                .email("viewer@example.com")
                .channelName("Seoul Viewer")
                .avatarUrl("https://api.dicebear.com/8.x/initials/svg?seed=SV")
                .channelDescription("Watches, comments, and tests the platform.")
                .build());
        User maker = userRepository.save(User.builder()
                .userName("maker")
                .email("maker@example.com")
                .channelName("Codec Notes")
                .avatarUrl("https://api.dicebear.com/8.x/initials/svg?seed=CN")
                .channelDescription("Practical notes on video delivery.")
                .build());

        Memo first = saveVideo(creator,
                "Building a multipart upload pipeline",
                "S3 multipart upload, presigned URLs, and a clean path toward MediaConvert HLS output.",
                "https://images.unsplash.com/photo-1492691527719-9d1e07e534b4?auto=format&fit=crop&w=1200&q=80",
                "https://media.w3.org/2010/05/bunny/trailer.mp4",
                596L,
                12840L,
                410L);
        Memo second = saveVideo(maker,
                "HLS streaming architecture explained",
                "A compact walkthrough of input buckets, Lambda triggers, MediaConvert jobs, and playback manifests.",
                "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=80",
                "https://media.w3.org/2010/05/sintel/trailer.mp4",
                888L,
                9210L,
                267L);
        saveVideo(creator,
                "Designing YouTube-style hover previews",
                "Preview scrubbing, metadata caching, and a React-first thumbnail interaction model.",
                "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=1200&q=80",
                "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                734L,
                6302L,
                198L);

        Comment top = commentRepository.save(Comment.builder()
                .memoId(first.getMno())
                .replyText("The multipart flow is much clearer with the direct-to-S3 split.")
                .build());
        top.setCommenter(viewer);
        Comment reply = commentRepository.save(Comment.builder()
                .memoId(first.getMno())
                .parentCommentId(top.getId())
                .replyText("Next step should be persisting the MediaConvert job state.")
                .build());
        reply.setCommenter(maker);

        Comment secondComment = commentRepository.save(Comment.builder()
                .memoId(second.getMno())
                .replyText("The HLS manifest path should probably live in the DB, not in the player.")
                .build());
        secondComment.setCommenter(viewer);

        videoLikeRepository.save(VideoLike.builder().memo(first).user(viewer).build());
        subscriptionRepository.save(Subscription.builder().subscriber(viewer).channelOwner(creator).build());
    }

    private Memo saveVideo(User uploader, String title, String description, String thumbnailUrl, String videoUrl,
                           Long durationSeconds, Long viewCount, Long likeCount) {
        Memo memo = Memo.builder()
                .memoText(title)
                .description(description)
                .imageUrl(thumbnailUrl)
                .videoUrl(videoUrl)
                .durationSeconds(durationSeconds)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .uploadStatus("READY")
                .visibility("PUBLIC")
                .build();
        memo.setUploader(uploader);
        return memoRepository.save(memo);
    }
}
