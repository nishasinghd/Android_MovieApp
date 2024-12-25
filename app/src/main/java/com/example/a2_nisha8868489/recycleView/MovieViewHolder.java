package com.example.a2_nisha8868489.recycleView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_nisha8868489.R;
import com.example.a2_nisha8868489.model.Movie;
import com.example.a2_nisha8868489.view.MovieDetailsActivity;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView title;
    TextView year;
    TextView rating;

    Context context;
    Movie movie; // Changed to Movie type

    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.thumbnailImageView);
        title = itemView.findViewById(R.id.titleTextView);
        year = itemView.findViewById(R.id.yearTextView);
        rating = itemView.findViewById(R.id.rateTextView);

        context = itemView.getContext(); // Initialize context here

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("movie", movie); // Sending the Movie object
            context.startActivity(intent);
        });
    }

    // Method to bind movie data to the view holder
    public void bind(Movie movie) {
        this.movie = movie;
        title.setText(movie.getTitle());
        year.setText(movie.getYear());
        rating.setText(movie.getRating());
    }
}