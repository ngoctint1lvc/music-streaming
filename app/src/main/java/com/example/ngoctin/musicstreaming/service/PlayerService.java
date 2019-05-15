package com.example.ngoctin.musicstreaming.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ngoctin.musicstreaming.R;
import com.example.ngoctin.musicstreaming.utils.YoutubeConnector;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.utils.YouTubePlayerStateTracker;

import java.lang.ref.WeakReference;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlayerService extends Service {
    private YouTubePlayer youtubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerStateTracker tracker;

    private View view;
    private String videoId = "6JYIGclVQdw";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerServiceBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tracker = new YouTubePlayerStateTracker();
        view = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.player_view, null);
        youTubePlayerView = view.findViewById(R.id.youtubePlayerView);
        initYouTubePlayerView();
        Log.d("Debug", "playerService oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        videoId = intent.getStringExtra("videoId");
        return START_NOT_STICKY;
    }

    private void initYouTubePlayerView() {
        youTubePlayerView.initialize((final YouTubePlayer ytPlayer) -> {
            youtubePlayer = ytPlayer;
            ytPlayer.addListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady() {
                    loadVideo(videoId);
                }
            });
            youtubePlayer = ytPlayer;
            youtubePlayer.addListener(tracker);
        }, true);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUIController().showYouTubeButton(false);
    }

    private void loadVideo(String videoId) {
        youtubePlayer.loadVideo(videoId, 0);
    }

    @Override
    public void onDestroy() {
        Log.d("Debug", "playerService ondestroy");
        youTubePlayerView.release();
        super.onDestroy();
    }

    public static class PlayerServiceBinder extends Binder {
        WeakReference<PlayerService> weakReference;

        PlayerServiceBinder(PlayerService playerService) {
            weakReference = new WeakReference<>(playerService);
        }

        public View getPlayerView() {
            return weakReference.get().view;
        }

        public YouTubePlayer getYoutubePlayer() {
            return weakReference.get().youtubePlayer;
        }

        public YouTubePlayerStateTracker getYoutubePlayerTracker() {
            return weakReference.get().tracker;
        }
    }
}