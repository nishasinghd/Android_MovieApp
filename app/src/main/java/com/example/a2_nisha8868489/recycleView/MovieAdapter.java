package com.example.a2_nisha8868489.recycleView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_nisha8868489.R;
import com.example.a2_nisha8868489.model.Movie;
import com.example.a2_nisha8868489.view.MovieDetailsActivity; // Import the MovieDetailsActivity
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private List<Movie> movieList; // List to hold movie data
    private Context context; // Context for loading resources

    // Constructor for the adapter
    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movies, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Get the movie at the current position
        Movie movie = movieList.get(position);

        // Bind the movie data to the ViewHolder
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        holder.rating.setText(movie.getRating());
        Picasso.get().load(movie.getImgSrc()).into(holder.image);

        // click listener for the item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("model", movie); // Pass the clicked movie to the MovieDetailsActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the movie list
        return movieList.size();
    }

    // Method to update the list of movies
    public void updateList(List<Movie> newList) {
        movieList = new ArrayList<>(newList);
        notifyDataSetChanged(); // Notify the adapter to refresh the UI
    }
}