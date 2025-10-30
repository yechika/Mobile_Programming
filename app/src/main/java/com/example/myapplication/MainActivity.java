package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.TMDBApi;
import com.example.myapplication.data.DummyData;
import com.example.myapplication.models.Movie;
import com.example.myapplication.models.MovieResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TMDBApi api;
    
    // Toggle untuk mode dummy data
    private static final boolean USE_DUMMY_DATA = true; // Ganti ke false jika sudah punya API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // Setup RecyclerView dengan optimasi
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true); // Optimasi jika ukuran item tetap
        recyclerView.setItemViewCacheSize(20); // Cache lebih banyak view
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        // Initialize Retrofit API
        api = RetrofitClient.getApi();

        // Load popular movies
        loadPopularMovies();
    }

    private void loadPopularMovies() {
        progressBar.setVisibility(View.VISIBLE);
        
        if (USE_DUMMY_DATA) {
            // Menggunakan dummy data (offline mode)
            loadDummyData();
        } else {
            // Menggunakan API TMDB (online mode)
            loadFromAPI();
        }
    }
    
    private void loadDummyData() {
        // Simulasi loading delay
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                List<Movie> dummyMovies = DummyData.getDummyMovies();
                movieAdapter.setMovies(dummyMovies);
                Toast.makeText(MainActivity.this, 
                    "Loaded " + dummyMovies.size() + " dummy movies", Toast.LENGTH_SHORT).show();
            }
        }, 500); // Delay 500ms untuk simulasi loading
    }
    
    private void loadFromAPI() {
        api.getPopularMovies(TMDBApi.API_KEY, "en-US", 1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> movies = response.body().getResults();
                            movieAdapter.setMovies(movies);
                        } else {
                            Toast.makeText(MainActivity.this, 
                                "Failed to load movies", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, 
                            "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bersihkan resources
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
    }
}
