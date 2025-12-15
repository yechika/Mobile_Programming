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
import com.example.myapplication.firebase.FirebaseManager;
import com.example.myapplication.firebase.AuthManager;
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

    // Toggle untuk mode data source
    private static final boolean USE_FIREBASE = true; // true = Firebase, false = TMDB API
    private static final boolean AUTO_INIT_FIREBASE = true; // Auto populate Firebase dengan dummy data jika kosong

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
        
        if (USE_FIREBASE) {
            // Menggunakan Firebase Realtime Database
            loadFromFirebase();
        } else {
            // Menggunakan API TMDB (online mode)
            loadFromAPI();
        }
    }
    
    private void loadFromFirebase() {
        // Check jika database kosong, initialize dengan dummy data
        if (AUTO_INIT_FIREBASE) {
            FirebaseManager.getInstance().isDatabaseEmpty(new FirebaseManager.FirebaseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean isEmpty) {
                    if (isEmpty) {
                        // Initialize Firebase dengan dummy data
                        List<Movie> dummyMovies = DummyData.getDummyMovies();
                        FirebaseManager.getInstance().initializeWithDummyData(dummyMovies, 
                            new FirebaseManager.FirebaseCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    // Setelah init, load movies
                                    loadMoviesFromFirebase();
                                }
                                
                                @Override
                                public void onError(Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, 
                                        "Error initializing database: " + e.getMessage(), 
                                        Toast.LENGTH_SHORT).show();
                                }
                            });
                    } else {
                        // Database sudah ada data, langsung load
                        loadMoviesFromFirebase();
                    }
                }
                
                @Override
                public void onError(Exception e) {
                    // Jika error checking, tetap coba load
                    loadMoviesFromFirebase();
                }
            });
        } else {
            loadMoviesFromFirebase();
        }
    }
    
    private void loadMoviesFromFirebase() {
        FirebaseManager.getInstance().getPopularMovies(new FirebaseManager.FirebaseCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> movies) {
                progressBar.setVisibility(View.GONE);
                movieAdapter.setMovies(movies);
                Toast.makeText(MainActivity.this, 
                    "Loaded " + movies.size() + " movies from Firebase", 
                    Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, 
                    "Error loading movies: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
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
        } else if (id == R.id.action_logout) {
            handleLogout();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void handleLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes", (dialog, which) -> {
                AuthManager.getInstance().signOut(this, new AuthManager.SignOutCallback() {
                    @Override
                    public void onSignOutSuccess() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            })
            .setNegativeButton("No", null)
            .show();
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
