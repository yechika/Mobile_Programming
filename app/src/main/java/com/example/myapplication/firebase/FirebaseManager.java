package com.example.myapplication.firebase;

import android.util.Log;
import com.example.myapplication.models.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Production-ready Firebase Realtime Database Manager
 * Handles all Firebase operations for movies
 */
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    private static final String MOVIES_NODE = "movies";
    private static final String POPULAR_MOVIES_NODE = "popular_movies";
    private static final String SEARCH_INDEX_NODE = "search_index";
    
    private static FirebaseManager instance;
    private final DatabaseReference databaseReference;
    
    private FirebaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Enable offline persistence
        database.setPersistenceEnabled(true);
        this.databaseReference = database.getReference();
    }
    
    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }
    
    /**
     * Get popular movies from Firebase
     */
    public void getPopularMovies(final FirebaseCallback<List<Movie>> callback) {
        databaseReference.child(POPULAR_MOVIES_NODE)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Movie> movies = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Movie movie = snapshot.getValue(Movie.class);
                            if (movie != null) {
                                movies.add(movie);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing movie: " + e.getMessage());
                        }
                    }
                    callback.onSuccess(movies);
                }
                
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error loading movies: " + databaseError.getMessage());
                    callback.onError(databaseError.toException());
                }
            });
    }
    
    /**
     * Search movies by title
     */
    public void searchMovies(String query, final FirebaseCallback<List<Movie>> callback) {
        if (query == null || query.trim().isEmpty()) {
            callback.onSuccess(new ArrayList<>());
            return;
        }
        
        String searchQuery = query.toLowerCase().trim();
        
        databaseReference.child(POPULAR_MOVIES_NODE)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Movie> results = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Movie movie = snapshot.getValue(Movie.class);
                            if (movie != null && movie.getTitle() != null) {
                                if (movie.getTitle().toLowerCase().contains(searchQuery)) {
                                    results.add(movie);
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing movie: " + e.getMessage());
                        }
                    }
                    callback.onSuccess(results);
                }
                
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error searching movies: " + databaseError.getMessage());
                    callback.onError(databaseError.toException());
                }
            });
    }
    
    /**
     * Get movie by ID
     */
    public void getMovieById(int movieId, final FirebaseCallback<Movie> callback) {
        databaseReference.child(POPULAR_MOVIES_NODE)
            .orderByChild("id")
            .equalTo(movieId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Movie movie = snapshot.getValue(Movie.class);
                        if (movie != null) {
                            callback.onSuccess(movie);
                            return;
                        }
                    }
                    callback.onError(new Exception("Movie not found"));
                }
                
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.toException());
                }
            });
    }
    
    /**
     * Add or update a movie in Firebase
     */
    public void saveMovie(Movie movie, final FirebaseCallback<Void> callback) {
        if (movie == null || movie.getId() == 0) {
            callback.onError(new IllegalArgumentException("Invalid movie data"));
            return;
        }
        
        databaseReference.child(POPULAR_MOVIES_NODE)
            .child(String.valueOf(movie.getId()))
            .setValue(movie)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Movie saved successfully");
                if (callback != null) {
                    callback.onSuccess(null);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error saving movie: " + e.getMessage());
                if (callback != null) {
                    callback.onError(e);
                }
            });
    }
    
    /**
     * Initialize database with dummy data (call this once)
     */
    public void initializeWithDummyData(List<Movie> movies, final FirebaseCallback<Void> callback) {
        DatabaseReference popularRef = databaseReference.child(POPULAR_MOVIES_NODE);
        
        for (Movie movie : movies) {
            popularRef.child(String.valueOf(movie.getId())).setValue(movie);
        }
        
        Log.d(TAG, "Database initialized with " + movies.size() + " movies");
        if (callback != null) {
            callback.onSuccess(null);
        }
    }
    
    /**
     * Check if database is empty
     */
    public void isDatabaseEmpty(final FirebaseCallback<Boolean> callback) {
        databaseReference.child(POPULAR_MOVIES_NODE)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    callback.onSuccess(!dataSnapshot.exists() || dataSnapshot.getChildrenCount() == 0);
                }
                
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.toException());
                }
            });
    }
    
    /**
     * Callback interface for Firebase operations
     */
    public interface FirebaseCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
