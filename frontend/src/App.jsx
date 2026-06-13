import { useEffect, useMemo, useRef, useState } from 'react'
import {
  Bell,
  Clapperboard,
  Heart,
  Home,
  LogIn,
  Maximize,
  Menu,
  Minimize,
  MoreVertical,
  Pause,
  Play,
  Plus,
  Reply,
  Search,
  Send,
  Upload,
  UserRound,
  Volume2,
  VolumeX,
} from 'lucide-react'
import './App.css'

async function api(path, options) {
  const isFormData = options?.body instanceof FormData
  const res = await fetch(path, {
    headers: { ...(isFormData ? {} : { 'Content-Type': 'application/json' }), ...(options?.headers || {}) },
    ...options,
  })
  if (!res.ok) {
    const text = await res.text()
    try {
      const parsed = JSON.parse(text)
      throw new Error(parsed.error || parsed.message || text)
    } catch {
      if (text.trim().startsWith('{')) {
        const parsed = JSON.parse(text)
        throw new Error(parsed.error || parsed.message || text)
      }
      throw new Error(text)
    }
  }
  return res.json()
}

function compact(value) {
  return new Intl.NumberFormat('en', { notation: 'compact' }).format(value || 0)
}

function timeAgo(value) {
  if (!value) return ''
  const diff = Math.max(1, Math.floor((Date.now() - new Date(value).getTime()) / 1000))
  if (diff < 60) return `${diff}s ago`
  if (diff < 3600) return `${Math.floor(diff / 60)}m ago`
  if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`
  return `${Math.floor(diff / 86400)}d ago`
}

function duration(seconds = 0) {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60).toString().padStart(2, '0')
  return `${mins}:${secs}`
}

function Header({ query, setQuery, currentUser, notifications, sidebarCollapsed, onHome, onUpload, onMyUploads, onLogin, onLogout, onMenu }) {
  const [profileOpen, setProfileOpen] = useState(false)
  const [notifOpen, setNotifOpen] = useState(false)

  return (
    <header className="topbar">
      <button className="icon-button" type="button" aria-label={sidebarCollapsed ? 'Open sidebar' : 'Close sidebar'} onClick={onMenu}>
        <Menu size={22} />
      </button>
      <button className="brand" type="button" onClick={onHome}>
        <Play size={18} fill="currentColor" /> seontube
      </button>
      <label className="search">
        <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search" />
        <button className="search-button" type="button" aria-label="Search">
          <Search size={22} />
        </button>
      </label>
      <button className="create-button" type="button" onClick={onUpload}>
        <Plus size={20} /> Create
      </button>
      <div className="notification">
        <button
          className="icon-button"
          type="button"
          aria-label="Notifications"
          onClick={() => setNotifOpen((open) => !open)}
        >
          <Bell size={22} />
          {notifications.length > 0 && <span className="notification-badge">{notifications.length}</span>}
        </button>
        {notifOpen && (
          <div className="notification-menu">
            <div className="notification-menu-head">
              <strong>Notifications</strong>
            </div>
            {notifications.length === 0 ? (
              <div className="notification-empty">
                <Bell size={30} />
                <p>아직 새로운 소식이 없어요</p>
                <span>새로운 좋아요·댓글·구독 소식이 생기면 여기에 표시됩니다.</span>
              </div>
            ) : (
              <ul className="notification-items">
                {notifications.map((note) => (
                  <li key={note.id}>
                    <strong>{note.actorName}</strong>
                    <p>{note.text}</p>
                    <span>{timeAgo(note.createdAt)}</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
      </div>
      <div className="profile-area">
        <button
          className="login-button"
          type="button"
          onClick={() => currentUser ? setProfileOpen((open) => !open) : onLogin()}
          title={currentUser ? 'Account menu' : 'Continue with Google'}
        >
          {currentUser?.avatarUrl ? <img src={currentUser.avatarUrl} alt="" /> : <LogIn size={18} />}
          <span>{currentUser?.channelName || 'Google Login'}</span>
        </button>
        {profileOpen && currentUser && (
          <div className="profile-menu">
            <strong>{currentUser.channelName}</strong>
            <span>{currentUser.email}</span>
            <button type="button" onClick={() => { setProfileOpen(false); onMyUploads() }}>My uploads</button>
            <button type="button" onClick={() => { setProfileOpen(false); onUpload() }}>Upload video</button>
            <button type="button" onClick={() => { setProfileOpen(false); onLogout() }}>Logout</button>
          </div>
        )}
      </div>
    </header>
  )
}

function Sidebar({ view, collapsed, onHome, onUpload, onLiked, onMyUploads, onSubscriptions }) {
  return (
    <aside className={`rail ${collapsed ? 'collapsed' : ''}`}>
      <button className={`rail-item ${view === 'home' ? 'active' : ''}`} onClick={onHome} type="button">
        <Home size={20} /> Home
      </button>
      <button className={`rail-item ${view === 'liked' ? 'active' : ''}`} onClick={onLiked} type="button">
        <Heart size={20} /> Liked
      </button>
      <button className={`rail-item ${view === 'myUploads' ? 'active' : ''}`} onClick={onMyUploads} type="button">
        <Clapperboard size={20} /> My videos
      </button>
      <button className={`rail-item ${view === 'subscriptions' ? 'active' : ''}`} onClick={onSubscriptions} type="button">
        <UserRound size={20} /> Subscriptions
      </button>
      <button className={`rail-item ${view === 'upload' ? 'active' : ''}`} onClick={onUpload} type="button">
        <Upload size={20} /> Upload
      </button>
    </aside>
  )
}

function VideoCollectionPage({ title, subtitle, videos, selected, onSelect, emptyIcon: EmptyIcon, emptyText }) {
  return (
    <section className="collection-page">
      <div className="collection-head">
        <h1>{title}</h1>
        <span>{subtitle}</span>
      </div>
      {videos.length === 0 ? (
        <div className="collection-empty">
          <EmptyIcon size={42} />
          <p>{emptyText}</p>
        </div>
      ) : (
        <div className="home-grid">
          {videos.map((video) => (
            <VideoCard key={video.id} video={video} active={selected?.id === video.id} onSelect={onSelect} />
          ))}
        </div>
      )}
    </section>
  )
}

function ChannelCollectionPage({ channels, onOpenChannel }) {
  return (
    <section className="collection-page">
      <div className="collection-head">
        <h1>Subscriptions</h1>
        <span>{channels.length} channels</span>
      </div>
      {channels.length === 0 ? (
        <div className="collection-empty">
          <UserRound size={42} />
          <p>아직 구독한 채널이 없어요.</p>
        </div>
      ) : (
        <div className="channel-grid">
          {channels.map((channel) => (
            <button className="channel-card" key={channel.id} type="button" onClick={() => onOpenChannel(channel)}>
              <img src={channel.avatarUrl} alt="" />
              <strong>{channel.channelName}</strong>
              <span>{compact(channel.subscriberCount)} subscribers</span>
            </button>
          ))}
        </div>
      )}
    </section>
  )
}

function VideoCard({ video, active, onSelect }) {
  const videoRef = useRef(null)
  const [hovering, setHovering] = useState(false)
  const [progress, setProgress] = useState(0)
  const [ready, setReady] = useState(false)
  const [playing, setPlaying] = useState(false)

  function startPreview() {
    setHovering(true)
    const el = videoRef.current
    if (!el) return
    el.muted = true
    el.play().catch(() => {})
  }

  function stopPreview() {
    setHovering(false)
    setProgress(0)
    const el = videoRef.current
    if (!el) return
    el.pause()
    el.currentTime = 0
  }

  function togglePlay(event) {
    event.stopPropagation()
    const el = videoRef.current
    if (!el) return
    if (el.paused) {
      el.muted = true
      el.play().catch(() => {})
    } else {
      el.pause()
    }
  }

  function seekPreview(event) {
    event.stopPropagation()
    const el = videoRef.current
    if (!el || !el.duration) return
    const next = Number(event.target.value)
    el.currentTime = (next / 100) * el.duration
    setProgress(next)
  }

  function syncProgress() {
    const el = videoRef.current
    if (!el || !el.duration) return
    setProgress((el.currentTime / el.duration) * 100)
  }

  return (
    <button
      className={`video-card ${active ? 'active' : ''}`}
      onClick={() => onSelect(video)}
      onMouseEnter={startPreview}
      onMouseLeave={stopPreview}
      type="button"
    >
      <div className="thumb">
        <img src={video.thumbnailUrl} alt="" className={hovering && ready ? 'hidden' : ''} />
        {!video.external && (
          <video
            ref={videoRef}
            src={video.videoUrl}
            className={hovering && ready ? '' : 'hidden'}
            muted
            playsInline
            preload="metadata"
            onCanPlay={() => setReady(true)}
            onTimeUpdate={syncProgress}
            onPlay={() => setPlaying(true)}
            onPause={() => setPlaying(false)}
          />
        )}
        {!video.external && <span className="duration">{duration(video.durationSeconds)}</span>}
        {hovering && !video.external && ready && (
          <button
            className="preview-play"
            type="button"
            onClick={togglePlay}
            onMouseDown={(event) => event.stopPropagation()}
            aria-label={playing ? 'Pause preview' : 'Play preview'}
          >
            {playing ? <Pause size={18} fill="currentColor" /> : <Play size={18} fill="currentColor" />}
          </button>
        )}
        {hovering && !video.external && (
          <input
            className="preview-scrubber"
            type="range"
            min="0"
            max="100"
            value={progress}
            onChange={seekPreview}
            onClick={(event) => event.stopPropagation()}
            onMouseDown={(event) => event.stopPropagation()}
            aria-label="Preview seek"
          />
        )}
      </div>
      <div className="video-meta">
        <img className="avatar" src={video.channel.avatarUrl} alt="" />
        <div>
          <h3>{video.title}</h3>
          <p>{video.channel.channelName}</p>
          <p>{compact(video.viewCount)} views · {timeAgo(video.createdAt)}</p>
        </div>
        <MoreVertical size={18} />
      </div>
    </button>
  )
}

function HomePage({ videos, selected, activeCategory, setActiveCategory, onSelect }) {
  const chips = ['All', 'Games', 'Music', 'Mixes', 'Live', 'Animation', 'Football', 'Action adventure games', 'Recently uploaded', 'Watched']
  return (
    <section className="home-page">
      <div className="chips">
        {chips.map((chip) => (
          <button className={activeCategory === chip ? 'selected' : ''} key={chip} type="button" onClick={() => setActiveCategory(chip)}>{chip}</button>
        ))}
      </div>
      <section className="promo">
        <div className="promo-media">
          <img src="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=80" alt="" />
          <div>
            <strong>seondev</strong>
            <h1>Large uploads, clean playback</h1>
          </div>
        </div>
        <div className="promo-copy">
          <div className="promo-mark">ST</div>
          <h2>Build a creator-grade video pipeline</h2>
          <p>Multipart upload, thumbnail previews, comments, notifications, and Google member login.</p>
        </div>
      </section>
      <div className="home-grid">
        {videos.map((video) => (
          <VideoCard key={video.id} video={video} active={selected?.id === video.id} onSelect={onSelect} />
        ))}
      </div>
    </section>
  )
}

function VideoPlayer({ video }) {
  const videoRef = useRef(null)
  const shellRef = useRef(null)
  const [playing, setPlaying] = useState(false)
  const [currentTime, setCurrentTime] = useState(0)
  const [totalTime, setTotalTime] = useState(0)
  const [muted, setMuted] = useState(false)
  const [volume, setVolume] = useState(1)
  const [fullscreen, setFullscreen] = useState(false)

  useEffect(() => {
    const el = videoRef.current
    if (!el) return
    el.load()
    setPlaying(false)
    setCurrentTime(0)
  }, [video.id])

  useEffect(() => {
    function onFsChange() {
      setFullscreen(document.fullscreenElement === shellRef.current)
    }
    document.addEventListener('fullscreenchange', onFsChange)
    return () => document.removeEventListener('fullscreenchange', onFsChange)
  }, [])

  function togglePlay() {
    const el = videoRef.current
    if (!el) return
    if (el.paused) el.play().catch(() => {})
    else el.pause()
  }

  function seek(event) {
    const el = videoRef.current
    if (!el || !el.duration) return
    const next = (Number(event.target.value) / 100) * el.duration
    el.currentTime = next
    setCurrentTime(next)
  }

  function toggleMute() {
    const el = videoRef.current
    if (!el) return
    el.muted = !el.muted
    setMuted(el.muted)
  }

  function changeVolume(event) {
    const el = videoRef.current
    const next = Number(event.target.value)
    setVolume(next)
    if (el) {
      el.volume = next
      el.muted = next === 0
    }
    setMuted(next === 0)
  }

  function toggleFullscreen() {
    const shell = shellRef.current
    if (!shell) return
    if (document.fullscreenElement) document.exitFullscreen()
    else shell.requestFullscreen?.()
  }

  const seekValue = totalTime ? (currentTime / totalTime) * 100 : 0

  return (
    <div className={`player-shell ${playing ? 'playing' : 'paused'}`} ref={shellRef}>
      <video
        ref={videoRef}
        className="player"
        src={video.videoUrl}
        poster={video.thumbnailUrl}
        playsInline
        onClick={togglePlay}
        onPlay={() => setPlaying(true)}
        onPause={() => setPlaying(false)}
        onTimeUpdate={() => setCurrentTime(videoRef.current?.currentTime || 0)}
        onLoadedMetadata={() => setTotalTime(videoRef.current?.duration || 0)}
        onEnded={() => setPlaying(false)}
      />
      <button className="player-overlay" type="button" onClick={togglePlay} aria-label={playing ? 'Pause' : 'Play'}>
        {!playing && <Play size={34} fill="currentColor" />}
      </button>
      <div className="player-controls" onClick={(event) => event.stopPropagation()}>
        <input
          className="player-seek"
          type="range"
          min="0"
          max="100"
          step="0.1"
          value={seekValue}
          onChange={seek}
          aria-label="Seek"
          style={{ '--played': `${seekValue}%` }}
        />
        <div className="player-buttons">
          <button type="button" className="player-icon" onClick={togglePlay} aria-label={playing ? 'Pause' : 'Play'}>
            {playing ? <Pause size={20} fill="currentColor" /> : <Play size={20} fill="currentColor" />}
          </button>
          <div className="player-volume">
            <button type="button" className="player-icon" onClick={toggleMute} aria-label={muted ? 'Unmute' : 'Mute'}>
              {muted || volume === 0 ? <VolumeX size={20} /> : <Volume2 size={20} />}
            </button>
            <input
              className="player-volume-slider"
              type="range"
              min="0"
              max="1"
              step="0.05"
              value={muted ? 0 : volume}
              onChange={changeVolume}
              aria-label="Volume"
            />
          </div>
          <span className="player-time">{duration(currentTime)} / {duration(totalTime)}</span>
          <button type="button" className="player-icon player-fullscreen" onClick={toggleFullscreen} aria-label={fullscreen ? 'Exit fullscreen' : 'Fullscreen'}>
            {fullscreen ? <Minimize size={20} /> : <Maximize size={20} />}
          </button>
        </div>
      </div>
    </div>
  )
}

function WatchPanel({ video, comments, commentText, setCommentText, replyTo, setReplyTo, currentUser, onComment, onLike, onSubscribe, onLogin }) {
  if (!video) {
    return (
      <section className="watch empty">
        <Clapperboard size={42} />
        <p>Select a video from the feed.</p>
      </section>
    )
  }

  return (
    <section className="watch">
      {video.external ? (
        <iframe className="player" src={video.externalUrl} title={video.title} allowFullScreen />
      ) : (
        <VideoPlayer video={video} />
      )}
      <h1>{video.title}</h1>
      <div className="watch-actions">
        <div className="channel-row">
          <img className="avatar large" src={video.channel.avatarUrl} alt="" />
          <div>
            <strong>{video.channel.channelName}</strong>
            <span>{compact(video.channel.subscriberCount)} subscribers</span>
          </div>
          <button className={`subscribe ${video.channel.subscribed ? 'subscribed' : ''}`} onClick={onSubscribe} type="button" disabled={video.external}>
            {video.channel.subscribed ? 'Subscribed' : 'Subscribe'}
          </button>
        </div>
        <button className={`pill ${video.liked ? 'selected' : ''}`} onClick={onLike} type="button" disabled={video.external}>
          <Heart size={18} fill={video.liked ? 'currentColor' : 'none'} />
          {compact(video.likeCount)}
        </button>
      </div>
      <div className="description">
        <strong>{compact(video.viewCount)} views · {timeAgo(video.createdAt)}</strong>
        <p>{video.description}</p>
      </div>
      <div className="comments-head">
        <h2>{video.commentCount} comments</h2>
      </div>
      <form className="comment-form" onSubmit={onComment}>
        <UserRound size={22} />
        <input
          value={commentText}
          onFocus={() => { if (!currentUser) onLogin() }}
          onChange={(event) => currentUser && setCommentText(event.target.value)}
          placeholder={currentUser ? (replyTo ? 'Write a reply...' : 'Add a comment...') : 'Sign in to comment...'}
          readOnly={!currentUser}
        />
        {replyTo && <button className="ghost" type="button" onClick={() => setReplyTo(null)}>Cancel</button>}
        <button className="icon-button primary" type="submit" aria-label="Send">
          <Send size={18} />
        </button>
      </form>
      <div className="comment-list">
        {comments.map((comment) => (
          <article className="comment" key={comment.id}>
            <img className="avatar" src={comment.author.avatarUrl} alt="" />
            <div>
              <strong>{comment.author.channelName} <span>{timeAgo(comment.createdAt)}</span></strong>
              <p>{comment.text}</p>
              <button className="reply-button" onClick={() => setReplyTo(comment)} type="button">
                <Reply size={14} /> Reply
              </button>
              {comment.replies?.map((reply) => (
                <article className="comment reply" key={reply.id}>
                  <img className="avatar" src={reply.author.avatarUrl} alt="" />
                  <div>
                    <strong>{reply.author.channelName} <span>{timeAgo(reply.createdAt)}</span></strong>
                    <p>{reply.text}</p>
                  </div>
                </article>
              ))}
            </div>
          </article>
        ))}
      </div>
    </section>
  )
}

function UploadPanel({ onCreate, currentUser, googleEnabled, onLogin }) {
  const [form, setForm] = useState({ title: '', description: '', thumbnailUrl: '', videoUrl: '', durationSeconds: 120 })
  const [thumbnailFile, setThumbnailFile] = useState(null)
  const [videoFile, setVideoFile] = useState(null)
  const [thumbnailUploading, setThumbnailUploading] = useState(false)
  const [videoUploading, setVideoUploading] = useState(false)
  const [thumbnailMessage, setThumbnailMessage] = useState('')
  const [videoMessage, setVideoMessage] = useState('')

  function update(event) {
    const { name, value } = event.target
    setForm((prev) => ({ ...prev, [name]: value }))
  }

  async function submit(event) {
    event.preventDefault()
    if (!currentUser) {
      onLogin()
      return
    }
    await onCreate({ ...form, uploaderId: currentUser.id, durationSeconds: Number(form.durationSeconds) || 120 })
    setForm({ title: '', description: '', thumbnailUrl: '', videoUrl: '', durationSeconds: 120 })
    setThumbnailFile(null)
    setVideoFile(null)
    setThumbnailMessage('')
    setVideoMessage('')
  }

  async function uploadThumbnail() {
    if (!thumbnailFile) return
    setThumbnailUploading(true)
    setThumbnailMessage('Uploading thumbnail...')
    try {
      const formData = new FormData()
      formData.append('file', thumbnailFile)
      const uploaded = await api('/api/uploads/thumbnail', { method: 'POST', body: formData })
      setForm((prev) => ({ ...prev, thumbnailUrl: uploaded.url }))
      setThumbnailMessage('Thumbnail uploaded.')
    } catch (error) {
      setThumbnailMessage(error.message || 'Thumbnail upload failed')
    } finally {
      setThumbnailUploading(false)
    }
  }

  async function uploadToS3() {
    if (!videoFile) return
    setVideoUploading(true)
    setVideoMessage('Preparing multipart upload...')
    try {
      const initiate = await api('/initiate-upload', { method: 'POST', body: JSON.stringify({ fileName: videoFile.name }) })
      const chunkSize = 10 * 1024 * 1024
      const chunkCount = Math.ceil(videoFile.size / chunkSize)
      const parts = []

      for (let partNumber = 1; partNumber <= chunkCount; partNumber += 1) {
        const start = (partNumber - 1) * chunkSize
        const chunk = videoFile.slice(start, Math.min(start + chunkSize, videoFile.size))
        setVideoMessage(`Uploading video part ${partNumber} / ${chunkCount}`)
        const signed = await api('/upload-signed-url', {
          method: 'POST',
          body: JSON.stringify({ fileName: initiate.fileName, uploadId: initiate.uploadId, partNumber }),
        })
        const put = await fetch(signed.preSignedUrl, { method: 'PUT', body: chunk })
        if (!put.ok) throw new Error(`S3 upload failed at part ${partNumber}`)
        parts.push({ partNumber, awsETag: put.headers.get('ETag')?.replaceAll('"', '') })
      }

      setVideoMessage('Completing video upload...')
      const completed = await api('/complete-upload', {
        method: 'POST',
        body: JSON.stringify({ fileName: initiate.fileName, uploadId: initiate.uploadId, parts }),
      })
      setForm((prev) => ({ ...prev, videoUrl: completed.url }))
      setVideoMessage('Video uploaded.')
    } catch (error) {
      setVideoMessage(error.message || 'Video upload failed')
    } finally {
      setVideoUploading(false)
    }
  }

  const canPublish = form.title.trim() && form.description.trim() && form.thumbnailUrl && form.videoUrl

  return (
    <section className="upload-view">
      {!currentUser && (
        <div className="login-required">
          <h2>Login required</h2>
          <p>Sign in with Google before uploading a video.</p>
          <button type="button" onClick={onLogin}>
            <LogIn size={18} /> Continue with Google
          </button>
          {!googleEnabled && <span>Google OAuth env values are not configured.</span>}
        </div>
      )}
      <form className="upload-panel" onSubmit={submit}>
        <div className="panel-title">
          <Upload size={18} />
          <h2>Upload video</h2>
        </div>
        <input name="title" value={form.title} onChange={update} placeholder="Video title" required />
        <textarea name="description" value={form.description} onChange={update} placeholder="Description" required />
        <label className="file-upload">
          <input type="file" accept="image/jpeg,image/png,image/webp" onChange={(event) => setThumbnailFile(event.target.files?.[0] || null)} />
          <span>{thumbnailFile ? thumbnailFile.name : 'Choose thumbnail image file'}</span>
        </label>
        <button className="secondary-action" type="button" onClick={uploadThumbnail} disabled={!currentUser || !thumbnailFile || thumbnailUploading}>
          <Upload size={16} /> {thumbnailUploading ? 'Uploading...' : 'Upload thumbnail'}
        </button>
        {thumbnailMessage && <p className="upload-message">{thumbnailMessage}</p>}
        {form.thumbnailUrl && <img className="thumbnail-preview" src={form.thumbnailUrl} alt="" />}
        <label className="file-upload">
          <input type="file" accept="video/*" onChange={(event) => setVideoFile(event.target.files?.[0] || null)} />
          <span>{videoFile ? videoFile.name : 'Choose video file for S3 multipart upload'}</span>
        </label>
        <button className="secondary-action" type="button" onClick={uploadToS3} disabled={!currentUser || !videoFile || videoUploading}>
          <Upload size={16} /> {videoUploading ? 'Uploading...' : 'Upload video'}
        </button>
        {videoMessage && <p className="upload-message">{videoMessage}</p>}
        <input name="durationSeconds" value={form.durationSeconds} onChange={update} type="number" min="1" />
        <button className="publish" type="submit" disabled={!currentUser || !canPublish}><Plus size={16} /> Publish</button>
      </form>
    </section>
  )
}

function App() {
  const [videos, setVideos] = useState([])
  const [selected, setSelected] = useState(null)
  const [comments, setComments] = useState([])
  const [notifications, setNotifications] = useState([])
  const [currentUser, setCurrentUser] = useState(null)
  const [googleEnabled, setGoogleEnabled] = useState(false)
  const [commentText, setCommentText] = useState('')
  const [replyTo, setReplyTo] = useState(null)
  const [query, setQuery] = useState('')
  const [view, setView] = useState('home')
  const [activeCategory, setActiveCategory] = useState('All')
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false)
  const [authError, setAuthError] = useState('')
  const [likedVideos, setLikedVideos] = useState([])
  const [myUploads, setMyUploads] = useState([])
  const [subscriptions, setSubscriptions] = useState([])

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase()
    return videos.filter((video) => {
      const haystack = `${video.title} ${video.channel.channelName} ${video.description}`.toLowerCase()
      const matchesSearch = !q || haystack.includes(q)
      const matchesCategory = activeCategory === 'All'
        || activeCategory === 'Recently uploaded'
        || haystack.includes(activeCategory.toLowerCase())
      return matchesSearch && matchesCategory
    })
  }, [activeCategory, query, videos])

  async function loadVideos() {
    const localVideos = await api(currentUser ? `/api/videos?viewerId=${currentUser.id}` : '/api/videos')
    const next = localVideos.length > 0 ? localVideos : await api('/api/youtube/videos?query=programming')
    setVideos(next)
    setSelected((current) => current ? next.find((video) => video.id === current.id) || next[0] : next[0])
  }

  async function loadComments(videoId) {
    if (!videoId || String(videoId).startsWith('yt-')) {
      setComments([])
      return
    }
    setComments(await api(`/api/videos/${videoId}/comments`))
  }

  async function loadNotifications() {
    if (!currentUser) {
      setNotifications([])
      return
    }
    setNotifications(await api(`/api/users/${currentUser.id}/notifications`))
  }

  useEffect(() => {
    async function boot() {
      const params = new URLSearchParams(window.location.search)
      const redirectedUserId = params.get('userId')
      const nextAuthError = params.get('authError')
      if (redirectedUserId) {
        localStorage.setItem('seontubeUserId', redirectedUserId)
        window.history.replaceState({}, '', window.location.pathname)
      }
      if (nextAuthError) {
        setAuthError('Google OAuth is not configured. Set GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET, then restart the backend.')
        window.history.replaceState({}, '', window.location.pathname)
      }
      const storedUserId = localStorage.getItem('seontubeUserId')
      const enabled = await api('/api/auth/google/enabled')
      const storedMember = storedUserId ? await api(`/api/auth/members/${storedUserId}`) : null
      const member = storedMember?.provider === 'GOOGLE' ? storedMember : null
      if (storedMember && storedMember.provider !== 'GOOGLE') {
        localStorage.removeItem('seontubeUserId')
      }
      setGoogleEnabled(Boolean(enabled))
      setCurrentUser(member)
      const localVideos = await api(member ? `/api/videos?viewerId=${member.id}` : '/api/videos')
      const next = localVideos.length > 0 ? localVideos : await api('/api/youtube/videos?query=programming')
      setVideos(next)
      setSelected((current) => current ? next.find((video) => video.id === current.id) || next[0] : next[0])
      if (member) {
        setNotifications(await api(`/api/users/${member.id}/notifications`))
      }
    }
    boot()
  }, [])

  useEffect(() => {
    if (!currentUser) return
    loadVideos()
    loadNotifications()
  }, [currentUser])

  useEffect(() => {
    loadComments(selected?.id)
  }, [selected?.id])

  async function submitComment(event) {
    event.preventDefault()
    if (!currentUser) {
      signInWithGoogle()
      return
    }
    if (!commentText.trim() || !selected || String(selected.id).startsWith('yt-')) return
    await api(`/api/videos/${selected.id}/comments`, {
      method: 'POST',
      body: JSON.stringify({ userId: currentUser.id, text: commentText, parentCommentId: replyTo?.id || null }),
    })
    setCommentText('')
    setReplyTo(null)
    await loadComments(selected.id)
    await loadVideos()
    await loadNotifications()
  }

  async function toggleLike() {
    if (!currentUser) {
      signInWithGoogle()
      return
    }
    if (!selected || String(selected.id).startsWith('yt-')) return
    const response = await api(`/api/videos/${selected.id}/like?userId=${currentUser.id}`, { method: 'POST' })
    setSelected((prev) => ({ ...prev, liked: response.active, likeCount: response.count }))
    setVideos((prev) => prev.map((video) => video.id === selected.id ? { ...video, liked: response.active, likeCount: response.count } : video))
    await loadNotifications()
  }

  async function toggleSubscription() {
    if (!currentUser) {
      signInWithGoogle()
      return
    }
    if (!selected || String(selected.id).startsWith('yt-')) return
    const response = await api(`/api/channels/${selected.channel.id}/subscribe?subscriberId=${currentUser.id}`, { method: 'POST' })
    const update = (video) => video.channel.id === selected.channel.id
      ? { ...video, channel: { ...video.channel, subscribed: response.active, subscriberCount: response.count } }
      : video
    setSelected((prev) => update(prev))
    setVideos((prev) => prev.map(update))
    await loadNotifications()
  }

  async function createVideo(payload) {
    if (!currentUser) {
      signInWithGoogle()
      return
    }
    const created = await api('/api/videos', { method: 'POST', body: JSON.stringify(payload) })
    setVideos((prev) => [created, ...prev])
    setSelected(created)
    setView('watch')
  }

  function openVideo(video) {
    setSelected(video)
    setView('watch')
  }

  async function openCollection(type) {
    if (!currentUser) {
      signInWithGoogle()
      return
    }
    try {
      if (type === 'liked') setLikedVideos(await api(`/api/users/${currentUser.id}/liked-videos`))
      if (type === 'myUploads') setMyUploads(await api(`/api/users/${currentUser.id}/uploads`))
      if (type === 'subscriptions') setSubscriptions(await api(`/api/users/${currentUser.id}/subscriptions`))
      setView(type)
    } catch (error) {
      setAuthError(error.message || 'Failed to load list')
    }
  }

  function openChannel(channel) {
    setQuery(channel.channelName)
    setView('home')
  }

  function signInWithGoogle() {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google'
  }

  async function logout() {
    await fetch('http://localhost:8080/logout', { method: 'POST', credentials: 'include' }).catch(() => {})
    localStorage.removeItem('seontubeUserId')
    setCurrentUser(null)
    setNotifications([])
    setView('home')
  }

  function requireLoginOrUpload() {
    if (!currentUser) {
      signInWithGoogle()
      return
    }
    setView('upload')
  }

  return (
    <div className="shell">
      <Header
        query={query}
        setQuery={setQuery}
        currentUser={currentUser}
        notifications={notifications}
        sidebarCollapsed={sidebarCollapsed}
        onHome={() => setView('home')}
        onUpload={requireLoginOrUpload}
        onMyUploads={() => openCollection('myUploads')}
        onLogin={signInWithGoogle}
        onLogout={logout}
        onMenu={() => setSidebarCollapsed((collapsed) => !collapsed)}
      />
      <Sidebar
        view={view}
        collapsed={sidebarCollapsed}
        onHome={() => setView('home')}
        onUpload={requireLoginOrUpload}
        onLiked={() => openCollection('liked')}
        onMyUploads={() => openCollection('myUploads')}
        onSubscriptions={() => openCollection('subscriptions')}
      />
      {authError && (
        <div className={`auth-banner ${sidebarCollapsed ? 'sidebar-collapsed' : ''}`}>
          {authError}
          <button type="button" onClick={() => setAuthError('')}>Dismiss</button>
        </div>
      )}
      <main className={`${['home', 'liked', 'myUploads', 'subscriptions'].includes(view) ? 'home-layout' : 'layout'} ${sidebarCollapsed ? 'sidebar-collapsed' : ''}`}>
        {view === 'home' && <HomePage videos={filtered} selected={selected} activeCategory={activeCategory} setActiveCategory={setActiveCategory} onSelect={openVideo} />}
        {view === 'watch' && (
          <>
            <WatchPanel
              video={selected}
              comments={comments}
              commentText={commentText}
              setCommentText={setCommentText}
              replyTo={replyTo}
              setReplyTo={setReplyTo}
              currentUser={currentUser}
              onComment={submitComment}
              onLike={toggleLike}
              onSubscribe={toggleSubscription}
              onLogin={signInWithGoogle}
            />
            <section className="feed">
              <div className="feed-head">
                <h2>Recommended</h2>
                <span>{filtered.length} videos</span>
              </div>
              <div className="video-grid">
                {filtered.map((video) => (
                  <VideoCard key={video.id} video={video} active={selected?.id === video.id} onSelect={openVideo} />
                ))}
              </div>
            </section>
          </>
        )}
        {view === 'liked' && (
          <VideoCollectionPage
            title="Liked videos"
            subtitle={`${likedVideos.length} videos`}
            videos={likedVideos}
            selected={selected}
            onSelect={openVideo}
            emptyIcon={Heart}
            emptyText="아직 좋아요 표시한 영상이 없어요."
          />
        )}
        {view === 'myUploads' && (
          <VideoCollectionPage
            title="My videos"
            subtitle={`${myUploads.length} videos`}
            videos={myUploads}
            selected={selected}
            onSelect={openVideo}
            emptyIcon={Clapperboard}
            emptyText="아직 업로드한 영상이 없어요. Create로 첫 영상을 올려보세요."
          />
        )}
        {view === 'subscriptions' && (
          <ChannelCollectionPage channels={subscriptions} onOpenChannel={openChannel} />
        )}
        {view === 'upload' && <UploadPanel onCreate={createVideo} currentUser={currentUser} googleEnabled={googleEnabled} onLogin={signInWithGoogle} />}
      </main>
    </div>
  )
}

export default App
