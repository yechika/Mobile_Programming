package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.FavoriteMovieAdapter;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.FavoriteMovie;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteMovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private AppDatabase database;

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

        // Initialize database
        database = AppDatabase.getInstance(this);

        // Load favorite movies
        loadFavoriteMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload favorites when returning to this activity
        loadFavoriteMovies();
    }

    private void loadFavoriteMovies() {
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, List<FavoriteMovie>>() {
            @Override
            protected List<FavoriteMovie> doInBackground(Void... voids) {
                return database.favoriteMovieDao().getAllFavorites();
            }

            @Override
            protected void onPostExecute(List<FavoriteMovie> favoriteMovies) {
                progressBar.setVisibility(View.GONE);
                
                if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    movieAdapter.setMovies(favoriteMovies);
                } else {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }.execute();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
    }
}
