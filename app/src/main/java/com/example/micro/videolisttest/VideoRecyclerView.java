package com.example.micro.videolisttest;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class VideoRecyclerView extends RecyclerView {
    private static final String TAG = VideoRecyclerView.class.getSimpleName();

    private ArrayList<MediaObject> mMediaList;

    private Context mContext;
    private FrameLayout mFrameLayout;
    private ImageView mThumbnail;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private int mPlayPosition = -1;
    private int mVideoSurfaceDefaultHeight = 0;
    private int mScreenDefaultHeight = 0;

    public VideoRecyclerView(Context context) {
        this(context, null);
    }

    public VideoRecyclerView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mVideoSurfaceDefaultHeight = point.x;
        mScreenDefaultHeight = point.y;

        mPlayerView = new PlayerView(context);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        mPlayerView.setUseController(false);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        mPlayerView.setPlayer(mExoPlayer);

        addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "TTT onScrollStateChanged state idle");
                    if (mThumbnail != null) {
                        mThumbnail.setVisibility(VISIBLE);
                    }
                }

//                if (!recyclerView.canScrollVertically(1)) {
                    playVideo();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        Log.d(TAG, "TTT buffering");
                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "TTT onPlayerStateChanged: Video ended.");
                        mExoPlayer.seekTo(0);
                        break;
                    case Player.STATE_READY:
                        Log.d(TAG, "TTT ready");
                        addVideoVew();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void playVideo() {
        int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();


        // if there is more than 2 list-items on the screen, set the difference to be 1
        if (endPosition - startPosition > 1) {
            endPosition = startPosition + 1;
        }

        // something is wrong. return.
        if (startPosition < 0 || endPosition < 0) {
            return;
        }

        int targetPosition = 0;
        //if there is more than 1 list-item on the screen
        if (startPosition != endPosition) {
            int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
            int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

            targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
        } else {
            targetPosition = mMediaList.size() - 1;
        }

        Log.d(TAG, "TTT playVideo: target position: " + targetPosition);
        if (targetPosition == mPlayPosition) {
            return;
        }

        mPlayPosition = targetPosition;
        if (mPlayerView == null) {
            Log.d(TAG, "TTT mPlayerView is null = "+(mPlayerView==null));
            return;
        }

        mPlayerView.setVisibility(INVISIBLE);
        Log.d(TAG, "TTT removeVideoView");
        removeVideoView(mPlayerView);

        int currentPosition = targetPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        View child = getChildAt(currentPosition);
        if (child == null) {
            Log.d(TAG, "TTT child is null");
            return;
        }

        Log.d(TAG, "TTT currentPosition = "+currentPosition);
        VideoRecyclerViewHolder.MyViewHolder holder =
                (VideoRecyclerViewHolder.MyViewHolder) findViewHolderForAdapterPosition(currentPosition);//child.getTag();
        if (holder == null) {
            Log.d(TAG, "TTT holder is null");
            mPlayPosition = -1;
            return;
        }

        mFrameLayout = holder.itemView.findViewById(R.id.media_container);
        mThumbnail = holder.mThumbNail;
        mPlayerView.setPlayer(mExoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "RecyclerView videoPlayer"));
        String url = mMediaList.get(targetPosition).getMedia_url();
        if (url != null) {
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            Log.d(TAG, "TTT prepare Exoplayer");
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "TTT getVisibleVideoSurfaceHeight at : "+at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);
        if (location[1] < 0) {
            return location[1] + mVideoSurfaceDefaultHeight;
        } else {
            return mScreenDefaultHeight - location[1];
        }
    }

    private void addVideoVew() {
        mFrameLayout.addView(mPlayerView);
        mPlayerView.requestFocus();
        mPlayerView.setVisibility(VISIBLE);
        mThumbnail.setVisibility(GONE);
    }

    private void removeVideoView(PlayerView playerView) {
        ViewGroup parent = (ViewGroup) playerView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(playerView);
        if (index >= 0) {
            parent.removeViewAt(index);
        }
    }

    public void setMediaObjects(ArrayList<MediaObject> list){
        this.mMediaList = list;
    }
}
