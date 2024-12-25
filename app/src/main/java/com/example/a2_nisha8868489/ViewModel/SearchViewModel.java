package com.example.a2_nisha8868489.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a2_nisha8868489.model.Movie;
import com.example.a2_nisha8868489.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchViewModel extends ViewModel {

    // LiveData to hold the list of movie results
    private MutableLiveData<List<Movie>> movieResults;
    // LiveData to hold any error messages
    private MutableLiveData<String> errorMessage;
    // Set to track fetched movie IDs to avoid duplicates
    private Set<String> fetchedIds;

    // Getter for movie results LiveData
    public MutableLiveData<List<Movie>> getMovieResults() {
        if (movieResults == null) {
            movieResults = new MutableLiveData<>(); // Initialize if null
        }
        return movieResults;
    }

    // Getter for error message LiveData
    public MutableLiveData<String> getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = new MutableLiveData<>(); // Initialize if null
        }
        return errorMessage;
    }

    // Method to fetch movies based on a search query
    public void fetchMovies(String query) {
        if (query.isEmpty()) return; // Return if query is empty

        // Construct the API URL for searching movies
        String baseUrl = "https://www.omdbapi.com/?s=" + query + "&apikey=db8958a0";

        // Make the network call to fetch movies
        ApiClient.get(baseUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("SearchViewModel", "API call failed", e);
                getErrorMessage().postValue("Failed to fetch movies"); // Post error message
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) return; // Return if response body is null
                String responseData = response.body().string(); // Get response data as a string

                try {
                    JSONObject jsonObject = new JSONObject(responseData); // Parse JSON response
                    if (!jsonObject.optString("Response").equalsIgnoreCase("True")) {
                        getErrorMessage().postValue("No movies found"); // Post error if no movies found
                        return;
                    }

                    JSONArray searchArray = jsonObject.optJSONArray("Search");
                    if (searchArray == null) return; // Return if search array is null

                    List<Movie> movies = new ArrayList<>(); // List to hold fetched movies
                    fetchedIds = new HashSet<>(); // Initialize fetched IDs set

                    // Loop through each movie in the search results
                    for (int i = 0; i < searchArray.length(); i++) {
                        String imdbId = searchArray.getJSONObject(i).optString("imdbID");
                        if (!fetchedIds.contains(imdbId)) { // Avoid duplicates
                            fetchedIds.add(imdbId);
                            fetchMovieDetails(imdbId, movies); // Fetch detailed movie info
                        }
                    }
                } catch (JSONException e) {
                    Log.e("SearchViewModel", "JSON Parsing error", e); // Log JSON parsing errors
                }
            }
        });
    }

    // Method to fetch detailed information for a specific movie
    private void fetchMovieDetails(String imdbId, List<Movie> movies) {
        // Construct the API URL for fetching movie details
        String detailsUrl = "https://www.omdbapi.com/?apikey=db8958a0&i=" + imdbId;

        // Make the network call to fetch movie details
        ApiClient.get(detailsUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("SearchViewModel", "Details API call failed", e);
                getErrorMessage().postValue("Failed to fetch movie details"); // Post error message
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) return; // Return if response body is null

                try {
                    JSONObject detailJson = new JSONObject(response.body().string()); // Parse detail JSON
                    Movie movie = new Movie(); // Create a new Movie object

                    // Set movie properties from the JSON data
                    movie.setTitle(detailJson.optString("Title"));
                    movie.setYear(detailJson.optString("Year"));
                    movie.setImgSrc(detailJson.optString("Poster"));
                    movie.setImdbId(detailJson.optString("imdbID"));
                    movie.setRating(detailJson.optString("imdbRating"));
                    movie.setPlot(detailJson.optString("Plot"));
                    movie.setAgeRating(detailJson.optString("Rated"));
                    movie.setReleased(detailJson.optString("Released"));
                    movie.setRuntime(detailJson.optString("Runtime"));
                    movie.setGenre(detailJson.optString("Genre"));
                    movie.setDirector(detailJson.optString("Director"));
                    movie.setLanguage(detailJson.optString("Language"));

                    movies.add(movie); // Add the movie to the list
                    getMovieResults().postValue(movies); // Update the LiveData

                } catch (JSONException e) {
                    Log.e("SearchViewModel", "Error parsing details JSON", e); // Log JSON parsing errors
                }
            }
        });
    }
}