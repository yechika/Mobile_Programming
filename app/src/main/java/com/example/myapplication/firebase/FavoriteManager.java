package com.example.myapplication.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.database.FavoriteMovie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager untuk mengelola favorit movies di Firebase Realtime Database
 * Struktur: /users/{userId}/favorites/{movieId}
 */
public class FavoriteManager {
    private static final String TAG = "FavoriteManager";
    private static FavoriteManager instance;
    
    private DatabaseReference databaseRef;
    private String currentUserId;
    
    private FavoriteManager() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }
    
    public static synchronized FavoriteManager getInstance() {
        if (instance == null) {
            instance = new FavoriteManager();
        }
        return instance;
    }
    
    /**
     * Set current user ID untuk operasi favorit
     */
    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }
    
    /**
     * Get reference ke favorites node user saat ini
     */
    private DatabaseReference getUserFavoritesRef() {
        if (currentUserId == null) {
            throw new IllegalStateException("User ID not set. Call setCurrentUserId() first.");
        }
        return databaseRef.child("users").child(currentUserId).child("favorites");
    }
    
    /**
     * Tambah movie ke favorit
     */
    public void addToFavorites(FavoriteMovie movie, OnCompleteListener listener) {
        try {
            DatabaseReference favoriteRef = getUserFavoritesRef().child(String.valueOf(movie.getId()));
            
            favoriteRef.setValue(movie)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Movie added to favorites: " + movie.getTitle());
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to add movie to favorites", e);
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error adding to favorites", e);
            if (listener != null) {
                listener.onFailure(e.getMessage());
            }
        }
    }
    
    /**
     * Hapus movie dari favorit
     */
    public void removeFromFavorites(long movieId, OnCompleteListener listener) {
        try {
            DatabaseReference favoriteRef = getUserFavoritesRef().child(String.valueOf(movieId));
            
            favoriteRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Movie removed from favorites: " + movieId);
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to remove movie from favorites", e);
                        if (listener != null) {
                            listener.onFailure(e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error removing from favorites", e);
            if (listener != null) {
                listener.onFailure(e.getMessage());
            }
        }
    }
    
    /**
     * Cek apakah movie ada di favorit
     */
    public void isFavorite(long movieId, OnCheckListener listener) {
        try {
            DatabaseReference favoriteRef = getUserFavoritesRef().child(String.valueOf(movieId));
            
            favoriteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean exists = snapshot.exists();
                    if (listener != null) {
                        listener.onResult(exists);
                    }
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error checking favorite status", error.toException());
                    if (listener != null) {
                        listener.onResult(false);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error checking favorite", e);
            if (listener != null) {
                listener.onResult(false);
            }
        }
    }
    
    /**
     * Load semua favorit movies user
     */
    public void loadFavorites(OnLoadListener listener) {
        try {
            DatabaseReference favoritesRef = getUserFavoritesRef();
            
            favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<FavoriteMovie> favorites = new ArrayList<>();
                    
                    for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                        try {
                            FavoriteMovie movie = movieSnapshot.getValue(FavoriteMovie.class);
                            if (movie != null) {
                                favorites.add(movie);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing movie data", e);
                        }
                    }
                    
                    Log.d(TAG, "Loaded " + favorites.size() + " favorite movies");
                    if (listener != null) {
                        listener.onLoaded(favorites);
                    }
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error loading favorites", error.toException());
                    if (listener != null) {
                        listener.onError(error.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error loading favorites", e);
            if (listener != null) {
                listener.onError(e.getMessage());
            }
        }
    }
    
    /**
     * Add real-time listener untuk favorit (untuk auto-update UI)
     */
    public void addFavoritesListener(OnLoadListener listener) {
        try {
            DatabaseReference favoritesRef = getUserFavoritesRef();
            
            favoritesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<FavoriteMovie> favorites = new ArrayList<>();
                    
                    for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                        try {
                            FavoriteMovie movie = movieSnapshot.getValue(FavoriteMovie.class);
                            if (movie != null) {
                                favorites.add(movie);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing movie data", e);
                        }
                    }
                    
                    if (listener != null) {
                        listener.onLoaded(favorites);
                    }
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error in favorites listener", error.toException());
                    if (listener != null) {
                        listener.onError(error.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error adding favorites listener", e);
            if (listener != null) {
                listener.onError(e.getMessage());
            }
        }
    }
    
    /**
     * Callback interface untuk operasi complete (add/remove)
     */
    public interface OnCompleteListener {
        void onSuccess();
        void onFailure(String error);
    }
    
    /**
     * Callback interface untuk cek favorit
     */
    public interface OnCheckListener {
        void onResult(boolean isFavorite);
    }
    
    /**
     * Callback interface untuk load favorit
     */
    public interface OnLoadListener {
        void onLoaded(List<FavoriteMovie> favorites);
        void onError(String error);
    }
}
