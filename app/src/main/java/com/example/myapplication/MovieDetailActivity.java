package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.database.FavoriteMovie;
import com.example.myapplication.firebase.FavoriteManager;
import com.example.myapplication.firebase.AuthManager;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView backdropImageView;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;
    private TextView overviewTextView;
    private ImageButton favoriteButton;
    private ScrollView scrollView;
    private ProgressBar progressBar;

    private int movieId;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double rating;
    private String releaseDate;
    private int voteCount;
    private boolean isFavorite = false;

    private FavoriteManager favoriteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Initialize views
        backdropImageView = findViewById(R.id.backdropImageView);
        posterImageView = findViewById(R.id.posterImageView);
        titleTextView = findViewById(R.id.titleTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        overviewTextView = findViewById(R.id.overviewTextView);
        favoriteButton = findViewById(R.id.favoriteButton);
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase FavoriteManager
        favoriteManager = FavoriteManager.getInstance();
        String userId = AuthManager.getInstance().getCurrentUserId();
        if (userId != null) {
            favoriteManager.setCurrentUserId(userId);
        } else {
            Toast.makeText(this, "Please login to use favorites", Toast.LENGTH_SHORT).show();
            favoriteButton.setEnabled(false);
        }

        // Get data from intent
        movieId = getIntent().getIntExtra("movie_id", 0);
        title = getIntent().getStringExtra("movie_title");
        overview = getIntent().getStringExtra("movie_overview");
        posterPath = getIntent().getStringExtra("movie_poster");
        backdropPath = getIntent().getStringExtra("movie_backdrop");
        rating = getIntent().getDoubleExtra("movie_rating", 0.0);
        releaseDate = getIntent().getStringExtra("movie_release_date");
        voteCount = getIntent().getIntExtra("movie_vote_count", 0);

        // Display movie details
        displayMovieDetails();

        // Check if movie is favorite
        checkFavoriteStatus();

        // Setup favorite button
        favoriteButton.setOnClickListener(v -> toggleFavorite());
    }

    private void displayMovieDetails() {
        titleTextView.setText(title);
        ratingTextView.setText(String.format("⭐ %.1f (%d votes)", rating, voteCount));
        releaseDateTextView.setText("Release Date: " + releaseDate);
        overviewTextView.setText(overview);

        // Load backdrop image
        if (backdropPath != null) {
            String backdropUrl = "https://image.tmdb.org/t/p/w780" + backdropPath;
            Glide.with(this)
                    .load(backdropUrl)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(backdropImageView);
        }

        // Load poster image
        if (posterPath != null) {
            String posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Glide.with(this)
                    .load(posterUrl)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(posterImageView);
        }
    }

    private void checkFavoriteStatus() {
        favoriteManager.isFavorite(movieId, isFav -> {
            isFavorite = isFav;
            updateFavoriteButton();
        });
    }

    private void toggleFavorite() {
        favoriteButton.setEnabled(false); // Disable sementara untuk prevent double-click
        
        if (isFavorite) {
            // Remove from favorites
            favoriteManager.removeFromFavorites(movieId, new FavoriteManager.OnCompleteListener() {
                @Override
                public void onSuccess() {
                    isFavorite = false;
                    updateFavoriteButton();
                    Toast.makeText(MovieDetailActivity.this, 
                        "Removed from favorites", Toast.LENGTH_SHORT).show();
                    favoriteButton.setEnabled(true);
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(MovieDetailActivity.this, 
                        "Failed to remove: " + error, Toast.LENGTH_SHORT).show();
                    favoriteButton.setEnabled(true);
                }
            });
        } else {
            // Add to favorites
            FavoriteMovie favoriteMovie = new FavoriteMovie(
                    movieId, title, overview, posterPath, backdropPath,
                    releaseDate, rating, voteCount, System.currentTimeMillis()
            );
            
            favoriteManager.addToFavorites(favoriteMovie, new FavoriteManager.OnCompleteListener() {
                @Override
                public void onSuccess() {
                    isFavorite = true;
                    updateFavoriteButton();
                    Toast.makeText(MovieDetailActivity.this, 
                        "Added to favorites", Toast.LENGTH_SHORT).show();
                    favoriteButton.setEnabled(true);
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(MovieDetailActivity.this, 
                        "Failed to add: " + error, Toast.LENGTH_SHORT).show();
                    favoriteButton.setEnabled(true);
                }
            });
        }
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bersihkan Glide untuk menghemat memori
        Glide.get(this).clearMemory();
    }
    
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Bersihkan cache saat memory rendah
        if (level >= TRIM_MEMORY_MODERATE) {
            Glide.get(this).clearMemory();
        }
    }
}
