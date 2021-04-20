package com.noahsyn.flicks_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.noahsyn.flicks_app.adapters.MovieAdapter;
import com.noahsyn.flicks_app.models.Movie;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;

import org.json.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;

public class MainActivity<baseUrl, backdropConfig> extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String CONFIG_URL = "https://api.themoviedb.org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(CONFIG_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                JSONObject configData = new JSONObject();
                try {
                    JSONObject configResults = jsonObject.getJSONObject("images");
                    Log.i(TAG, "Result: " + configResults.toString());
                    // Append new data to configData
                    configData.put("baseUrl", configResults.getString("secure_base_url"));
                    configData.put("posterSize", configResults.getJSONArray("poster_sizes").getString(3));
                    configData.put("backdropSize", configResults.getJSONArray("backdrop_sizes").getString(1));
                    saveConfig(configData);
                    Log.d(TAG, "Config: " + String.valueOf(configData));

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromApiResult(results, readConfig()));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private File getConfigFile() {
        return new File(getFilesDir(), "config.json");
    };

    private JSONObject readConfig() {
        try {
            JSONObject configData = new JSONObject(FileUtils.readFileToString(getConfigFile()));
            Log.i("ConfigUtils", "Read: " + configData.toString());
            return configData;
        } catch (IOException | JSONException e) {
            Log.e("MainActivity", "Error reading items", e);
            return new JSONObject();
        }
    }

    private void saveConfig(JSONObject configData) {
        if (configData.length() > 0 && !configData.toString().equals(readConfig().toString())) {
            Log.i("ConfigUtils", configData.toString());
            try {
                FileUtils.writeStringToFile(getConfigFile(), configData.toString());
            } catch (IOException e) {
                Log.e("MainActivity", "Error writing items", e);
            }
        }
    }
}