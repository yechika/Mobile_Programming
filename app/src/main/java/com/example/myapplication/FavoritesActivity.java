package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.FavoriteMovieAdapter;
import com.example.myapplication.database.FavoriteMovie;
import com.example.myapplication.firebase.FavoriteManager;
import com.example.myapplication.firebase.AuthManager;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteMovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private FavoriteManager favoriteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        emptyTextView = findViewById(R.id.emptyTextView);

        // Setup RecyclerView dengan optimasi
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        movieAdapter = new FavoriteMovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        // Initialize Firebase FavoriteManager
        favoriteManager = FavoriteManager.getInstance();
        String userId = AuthManager.getInstance().getCurrentUserId();
        if (userId != null) {
            favoriteManager.setCurrentUserId(userId);
            // Load favorite movies with real-time updates
            loadFavoriteMovies();
        } else {
            Toast.makeText(this, "Please login to view favorites", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("Please login to view favorites");
        }
    }

    private void loadFavoriteMovies() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Gunakan real-time listener untuk auto-update saat ada perubahan
        favoriteManager.addFavoritesListener(new FavoriteManager.OnLoadListener() {
            @Override
            public void onLoaded(List<FavoriteMovie> favorites) {
                progressBar.setVisibility(View.GONE);
                
                if (favorites != null && !favorites.isEmpty()) {
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    movieAdapter.setMovies(favorites);
                } else {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    emptyTextView.setText("No favorite movies yet");
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FavoritesActivity.this, 
                    "Error loading favorites: " + error, Toast.LENGTH_SHORT).show();
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText("Error loading favorites");
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
    }
}
