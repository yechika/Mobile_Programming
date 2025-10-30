package com.example.myapplication;

import android.os.AsyncTask;
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
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.FavoriteMovie;

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

    private AppDatabase database;

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

        // Initialize database
        database = AppDatabase.getInstance(this);

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
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return database.favoriteMovieDao().isFavorite(movieId);
            }

            @Override
            protected void onPostExecute(Boolean favorite) {
                isFavorite = favorite;
                updateFavoriteButton();
            }
        }.execute();
    }

    private void toggleFavorite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (isFavorite) {
                    // Remove from favorites
                    database.favoriteMovieDao().deleteById(movieId);
                } else {
                    // Add to favorites
                    FavoriteMovie favoriteMovie = new FavoriteMovie(
                            movieId, title, overview, posterPath, backdropPath,
                            releaseDate, rating, voteCount, System.currentTimeMillis()
                    );
                    database.favoriteMovieDao().insert(favoriteMovie);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                isFavorite = !isFavorite;
                updateFavoriteButton();
                String message = isFavorite ? 
                    "Added to favorites" : "Removed from favorites";
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }.execute();
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
