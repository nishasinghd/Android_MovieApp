package com.example.a2_nisha8868489.recycleView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a2_nisha8868489.R;
import com.example.a2_nisha8868489.model.Movie;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private Context context;
    private List<Movie> favouriteMovies;
    private FavouritesViewModel favouritesViewModel;

    public FavouritesAdapter(Context context, FavouritesViewModel favouritesViewModel) {
        this.context = context;
        this.favouritesViewModel = favouritesViewModel;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favourite_movie, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {

        Movie movie = favouriteMovies.get(position);

        holder.titleTextView.setText(movie.getTitle());

        Glide.with(context)
                .load(movie.getImgSrc())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.posterImageView);

        holder.removeMovieButton.setOnClickListener(v -> {
            int positionToRemove = holder.getAdapterPosition();
            if (positionToRemove != RecyclerView.NO_POSITION) {
                Movie movieToRemove = favouriteMovies.get(positionToRemove);

                // Check for a valid movie title
                if (movieToRemove.getTitle() != null && !movieToRemove.getTitle().isEmpty()) {
                    // Delete movie using the ViewModel by title
                    favouritesViewModel.deleteFavMovieByTitle(movieToRemove.getTitle(), favouritesViewModel.getUserId());
                    Toast.makeText(context, "Movie removed from favourites.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("FavouritesAdapter", "Invalid movie title. Cannot delete.");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouriteMovies != null ? favouriteMovies.size() : 0;
    }

    public void setFavouriteMovies(List<Movie> movies) {
        this.favouriteMovies = movies;
        notifyDataSetChanged();
    }

    public static class FavouritesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView posterImageView;
        Button removeMovieButton;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            posterImageView = itemView.findViewById(R.id.thumbnailImageView);
            removeMovieButton = itemView.findViewById(R.id.removeMovieButton);
        }
    }
}