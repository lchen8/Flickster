package com.example.lily_chen.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lily_chen.flickster.adapters.MovieArrayAdapter;
import com.example.lily_chen.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    SwipeRefreshLayout swipeContainer;
    String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        this.loadMovies();
        this.setupSwipeContainer();
        this.setupListViewListener();

    }

    public void launchInfoView(Movie movie) {
        Intent i = new Intent(this, MovieInfoActivity.class);
        i.putExtra("movie", movie);
        startActivity(i);
    }

    public void launchPlayTrailerView(String url) {
        Intent i = new Intent(this, PlayTrailerActivity.class);
        i.putExtra("url", url);
        startActivity(i);
    }

    private void setupListViewListener() {
        // single click launches new activity to edit the item
        lvItems.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View item, final int pos, long id) {
                    Movie selectedMovie = movies.get(pos);
                    if (selectedMovie.getDisplayType() == Movie.DisplayType.BACKDROP_ONLY) {
                        launchPlayTrailerView(selectedMovie.getTrailerURL());
                    } else {
                        launchInfoView(selectedMovie);
                    }
                }
            }
        );
    }

    private void loadMovies(){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;

                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d("DEBUG", movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void setupSwipeContainer(){
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.scMovies);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovies();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
}
