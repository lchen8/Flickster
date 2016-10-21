package com.example.lily_chen.flickster;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.lily_chen.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by lily_chen on 10/20/16.
 */
public class MovieInfoActivity extends YouTubeBaseActivity {

    Movie movie;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        movie = getIntent().getParcelableExtra("movie");

        YouTubePlayerView infoMoviePoster = (YouTubePlayerView) findViewById(R.id.infoPlayer);
        TextView infoTitle = (TextView) findViewById(R.id.infoTitle);
        TextView infoOverview = (TextView) findViewById(R.id.infoOverview);
        RatingBar infoRating = (RatingBar) findViewById(R.id.infoRatingBar);
        TextView infoDate = (TextView) findViewById(R.id.infoDate);
        TextView infoVotes = (TextView) findViewById(R.id.infoVotes);

        infoTitle.setText(movie.getOriginalTitle());
        infoOverview.setText(movie.getOverview());
        infoRating.setRating((float)movie.getRating());
        infoDate.setText(String.format("Released: %s", movie.getDate()));
        infoVotes.setText(String.format(
                "Average: %s/10, %s vote(s)", "" + movie.getRating(), "" + movie.getVotes()
        ));

        setupYouTubePlayerView();

    }

    private void setupYouTubePlayerView () {
        final YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.infoPlayer);

        youTubePlayerView.initialize("AIzaSyBixlvO0eA9f3N4C9hFzSftNt32CwtZmVc",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(movie.getTrailerURL());
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        youTubePlayerView.setBackground(getDrawable(R.drawable.video_error));
                    }
                });
    }

}
