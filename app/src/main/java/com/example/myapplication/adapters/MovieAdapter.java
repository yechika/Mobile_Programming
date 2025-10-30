package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MovieDetailActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movieList;
    private static final RequestOptions glideOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher);

    public MovieAdapter(Context context) {
        this.context = context;
        this.movieList = new ArrayList<>();
        setHasStableIds(true); // Optimasi untuk RecyclerView
    }

    public void setMovies(List<Movie> movies) {
        this.movieList = movies != null ? movies : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return movieList.get(position).getId();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
    
    @Override
    public void onViewRecycled(@NonNull MovieViewHolder holder) {
        super.onViewRecycled(holder);
        // Bersihkan image untuk menghemat memori - dengan safety check
        try {
            if (context instanceof android.app.Activity) {
                android.app.Activity activity = (android.app.Activity) context;
                if (!activity.isDestroyed() && !activity.isFinishing()) {
                    Glide.with(context).clear(holder.posterImageView);
                }
            } else {
                Glide.with(context).clear(holder.posterImageView);
            }
        } catch (Exception e) {
            // Ignore jika activity sudah destroyed
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView posterImageView;
        TextView titleTextView;
        TextView ratingTextView;
        TextView releaseDateTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            ratingTextView.setText(String.format("⭐ %.1f", movie.getVoteAverage()));
            releaseDateTextView.setText(movie.getReleaseDate());

            // Load poster image dengan Glide - optimized
            if (movie.getPosterPath() != null) {
                Glide.with(context)
                        .load(movie.getFullPosterPath())
                        .apply(glideOptions)
                        .into(posterImageView);
            } else {
                posterImageView.setImageResource(R.mipmap.ic_launcher);
            }

            // Click listener
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movie_id", movie.getId());
                intent.putExtra("movie_title", movie.getTitle());
                intent.putExtra("movie_overview", movie.getOverview());
                intent.putExtra("movie_poster", movie.getPosterPath());
                intent.putExtra("movie_backdrop", movie.getBackdropPath());
                intent.putExtra("movie_rating", movie.getVoteAverage());
                intent.putExtra("movie_release_date", movie.getReleaseDate());
                intent.putExtra("movie_vote_count", movie.getVoteCount());
                context.startActivity(intent);
            });
        }
    }
}
