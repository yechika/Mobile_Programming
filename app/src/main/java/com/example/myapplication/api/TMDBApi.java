package com.example.myapplication.api;

import com.example.myapplication.models.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBApi {
    // You need to get your own API key from https://www.themoviedb.org/settings/api
    // TEMPORARY API KEY - Replace with your own from https://www.themoviedb.org/settings/api
    String API_KEY = "8e7c8f3c8a8d8f3c8a8d8f3c8a8d8f3c"; // Replace with your actual API key
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );
}
