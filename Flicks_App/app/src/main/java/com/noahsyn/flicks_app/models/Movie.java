package com.noahsyn.flicks_app.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {

    String baseUrl;
    String posterSize;
    String posterPath;
    String backdropSize;
    String backdropPath;
    String title;
    String overview;
    String releaseDate;
    int movieID;
    double rating;

    // empty constructor needed fr Parceler Library
    public Movie() {}

    public Movie(JSONObject movieData, JSONObject configData) throws JSONException {
        Log.i("MovieInit", configData.toString());
        baseUrl = configData.getString("baseUrl");
        posterSize = configData.getString("posterSize");
        backdropSize = configData.getString("backdropSize");

        movieID = movieData.getInt("id");
        posterPath = movieData.getString("poster_path");
        backdropPath = movieData.getString("backdrop_path");
        title = movieData.getString("title");
        overview = movieData.getString("overview");
        releaseDate = movieData.getString("release_date");
        rating = movieData.getDouble("vote_average");
    }

    public static List<Movie> fromApiResult(JSONArray movieJsonArray, JSONObject configData) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i), configData));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format(baseUrl + posterSize + posterPath);
    }

    public String getBackdropPath() {
        return String.format(baseUrl + backdropSize + "/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() { return releaseDate; }

    public double getRating() { return rating; }

    public int getMovieID() { return movieID; }
}

