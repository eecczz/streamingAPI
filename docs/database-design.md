# YouTube Clone Database Design

This project keeps the original naming direction while tightening each table into a video platform domain.

## Current Domain Mapping

| Domain concept | Current table/entity | Role |
| --- | --- | --- |
| Member / channel | `user` / `User` | Login identity placeholder and channel profile |
| Video post | `tbl_memo` / `Memo` | Uploaded video metadata, thumbnail, playback URL, counters, status |
| Comment / reply | `comment` / `Comment` | Top-level comments and nested replies using `parentCommentId` |
| Notification | `note` / `Note` | Like, comment, reply, and subscribe events for a target user |
| Subscription | `subscription` / `Subscription` | Viewer subscribes to a channel owner |
| Legacy follow bucket | `follow_user`, `follower` | Existing follow model kept for compatibility |
| Video like | `video_like` / `VideoLike` | One like per user per video |

## Main Tables

### `user`

- `id`: primary key
- `userName`: account identifier
- `channelName`: display channel name
- `avatarUrl`: profile image
- `channelDescription`: channel introduction

Future production fields: email, password hash, role, provider, channel handle, created/updated timestamps.

### `tbl_memo`

`Memo` is the current video-post table.

- `mno`: primary key
- `memoText`: video title
- `description`: video description
- `imageUrl`: thumbnail URL
- `videoUrl`: original MP4 or completed S3 URL
- `hlsUrl`: HLS manifest URL after MediaConvert
- `durationSeconds`: video length
- `viewCount`, `likeCount`: cached counters
- `visibility`: `PUBLIC`, `UNLISTED`, `PRIVATE`
- `uploadStatus`: `UPLOADING`, `UPLOADED`, `TRANSCODING`, `READY`, `FAILED`
- `createdAt`
- `uploader_id`: channel owner

Future production split: move files into `video_asset`, transcoding into `transcoding_job`, and analytics into `video_view`.

### `comment`

- `id`: primary key
- `replyText`: comment body
- `commenter_id`: writer
- `memoId`: target video
- `parentCommentId`: null for top-level comments, set for replies
- `likeCount`
- `createdAt`

Future production fields: editedAt, deletedAt, pinned, moderation status.

### `video_like`

- `id`: primary key
- `memo_id`: liked video
- `user_id`: liker
- `createdAt`
- unique key: `(memo_id, user_id)`

### `subscription`

- `id`: primary key
- `subscriber_id`: viewer
- `channel_owner_id`: channel being subscribed to
- `createdAt`
- unique key: `(subscriber_id, channel_owner_id)`

### `note`

- `id`: primary key
- `noteText`: message shown to the receiver
- `targetUser_id`: notification receiver
- `notingUserName`: actor display name
- `notificationType`: `LIKE`, `COMMENT`, `REPLY`, `SUBSCRIBE`
- `sourceMemoId`
- `sourceCommentId`
- `notification_read`
- `createdAt`

## Recommended Next Backend Step

When AWS is ready, add:

- `upload_session`: multipart upload id, object key, uploader, status
- `transcoding_job`: MediaConvert job id, input key, output key, status, error message
- `video_asset`: original object URL, HLS master manifest URL, thumbnail URL, resolutions
- S3 event/Lambda callback endpoint or EventBridge listener to update `uploadStatus`
