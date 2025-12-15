package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.MovieAdapter;
import com.example.myapplication.api.RetrofitClient;
import com.example.myapplication.api.TMDBApi;
import com.example.myapplication.data.DummyData;
import com.example.myapplication.models.Movie;
import com.example.myapplication.models.MovieResponse;
import com.example.myapplication.firebase.FirebaseManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private TextView resultCountTextView;
    private TMDBApi api;
    
    // Toggle untuk mode data source
    private static final boolean USE_FIREBASE = true; // true = Firebase, false = TMDB API
    
    // Debounce untuk real-time search
    private Handler searchHandler;
    private Runnable searchRunnable;
    private static final long SEARCH_DELAY = 500; // 500ms delay untuk debouncing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize handler untuk debouncing
        searchHandler = new Handler(Looper.getMainLooper());

        // Initialize views
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        
        // Tambahkan empty state dan result count jika ada di layout
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        resultCountTextView = findViewById(R.id.resultCountTextView);

        // Setup RecyclerView dengan optimasi
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        // Initialize Retrofit API
        api = RetrofitClient.getApi();

        // Setup SearchView
        setupSearchView();
        
        // Auto focus pada search
        searchView.setIconified(false);
        searchView.requestFocus();
        
        // Show empty state initially
        showEmptyState(true, "Search for movies...");
    }
    
    private void setupSearchView() {
        searchView.setQueryHint("Search movies...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    searchMovies(query.trim());
                    searchView.clearFocus(); // Hide keyboard
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Real-time search dengan debouncing
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                
                if (newText.trim().isEmpty()) {
                    movieAdapter.setMovies(new ArrayList<>());
                    showEmptyState(true, "Search for movies...");
                    if (resultCountTextView != null) {
                        resultCountTextView.setVisibility(View.GONE);
                    }
                    return true;
                }
                
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (newText.trim().length() >= 2) { // Minimal 2 karakter
                            searchMovies(newText.trim());
                        }
                    }
                };
                
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY);
                return true;
            }
        });
        
        // Clear button listener
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                movieAdapter.setMovies(new ArrayList<>());
                showEmptyState(true, "Search for movies...");
                if (resultCountTextView != null) {
                    resultCountTextView.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void searchMovies(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }
        
        showLoading(true);
        showEmptyState(false, null);
        
        if (USE_FIREBASE) {
            searchFromFirebase(query);
        } else {
            searchFromAPI(query);
        }
    }
    
    private void searchFromFirebase(String query) {
        FirebaseManager.getInstance().searchMovies(query, new FirebaseManager.FirebaseCallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> results) {
                showLoading(false);
                displayResults(results, query);
            }
            
            @Override
            public void onError(Exception e) {
                showLoading(false);
                showError("Error searching: " + e.getMessage());
            }
        });
    }
    
    private void searchDummyData(String query) {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoading(false);
                List<Movie> results = DummyData.searchMovies(query);
                displayResults(results, query);
            }
        }, 300);
    }
    
    private void searchFromAPI(String query) {
        api.searchMovies(TMDBApi.API_KEY, "en-US", query, 1)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Movie> movies = response.body().getResults();
                            displayResults(movies != null ? movies : new ArrayList<>(), query);
                        } else {
                            showError("Failed to search movies");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        showLoading(false);
                        showError("Error: " + t.getMessage());
                    }
                });
    }
    
    private void displayResults(List<Movie> results, String query) {
        movieAdapter.setMovies(results);
        
        if (results.isEmpty()) {
            showEmptyState(true, "No results found for \"" + query + "\"");
            if (resultCountTextView != null) {
                resultCountTextView.setVisibility(View.GONE);
            }
        } else {
            showEmptyState(false, null);
            if (resultCountTextView != null) {
                resultCountTextView.setVisibility(View.VISIBLE);
                resultCountTextView.setText(results.size() + " movie(s) found");
            }
        }
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    private void showEmptyState(boolean show, String message) {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show && message != null) {
                emptyStateTextView.setText(message);
            }
        }
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showEmptyState(true, message);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Cleanup handler
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        
        // Cleanup RecyclerView
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
    }
}
