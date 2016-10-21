package com.example.lily_chen.flickster.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lily_chen on 10/17/16.
 */
public class Movie implements Parcelable {

    int id;
    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    int votes;
    Double rating;
    String date;
    String trailerURL;
    DisplayType displayType;

    public enum DisplayType {
        BACKDROP_ONLY, NORMAL
    }

    public int getId() { return id; }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/original/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/original/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public int getVotes() { return votes; }

    public double getRating() { return rating; }

    public String getDate() { return date; }

    public String getTrailerURL() { return trailerURL; }

    public DisplayType getDisplayType() { return displayType; }

    public Movie(JSONObject jsonObject) throws JSONException{
        this.id = jsonObject.getInt("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.votes = jsonObject.getInt("vote_count");
        this.rating = jsonObject.getDouble("vote_average");
        this.date = jsonObject.getString("release_date");
        getTrailerURLFromID();

        // for popular movies, only display the backdrop image
        if (this.rating >= 5.0) {
            this.displayType = DisplayType.BACKDROP_ONLY;
        } else {
            this.displayType = DisplayType.NORMAL;
        }
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();
        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private void getTrailerURLFromID() {
        String id = "" + this.id;
        String url = String.format("https://api.themoviedb.org/3/movie/%s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", id);

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailerJsonResults = null;

                try {
                    trailerJsonResults = response.getJSONArray("youtube");
                    trailerURL = trailerJsonResults.getJSONObject(0).getString("source");
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

    private Movie(Parcel in){
        id = in.readInt();
        posterPath = in.readString();
        backdropPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        votes = in.readInt();
        rating = in.readDouble();
        date = in.readString();
        trailerURL = in.readString();
    }


    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeString(posterPath);
        out.writeString(backdropPath);
        out.writeString(originalTitle);
        out.writeString(overview);
        out.writeInt(votes);
        out.writeDouble(rating);
        out.writeString(date);
        out.writeString(trailerURL);
        // we do not pass display type because parcel cannot handle enums
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
