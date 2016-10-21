package com.example.lily_chen.flickster;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by lily_chen on 10/21/16.
 */
public class PlayTrailerActivity extends YouTubeBaseActivity {

    String url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_trailer);
        url = getIntent().getStringExtra("url");

        final YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.trailerPlayer);

        youTubePlayerView.initialize("AIzaSyBixlvO0eA9f3N4C9hFzSftNt32CwtZmVc",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo(url);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        youTubePlayerView.setBackground(getDrawable(R.drawable.video_error));
                    }
                });
    }
}
