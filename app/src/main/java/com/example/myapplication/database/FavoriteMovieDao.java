package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Insert
    void insert(FavoriteMovie movie);

    @Delete
    void delete(FavoriteMovie movie);

    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    List<FavoriteMovie> getAllFavorites();

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId LIMIT 1")
    FavoriteMovie getFavoriteById(int movieId);

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    void deleteById(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    boolean isFavorite(int movieId);
}
