package com.example.a2_nisha8868489.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2_nisha8868489.R;
import com.example.a2_nisha8868489.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity {

    private String originalPlot;
    private Movie model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        // Retrieve the movie object from the intent
        model = (Movie) getIntent().getSerializableExtra("model");

        if (model != null) {
            Log.d("MovieDetailsActivity", "Movie title: " + model.getTitle());
            ((TextView) findViewById(R.id.titleTextView)).setText(model.getTitle());
            ((TextView) findViewById(R.id.ageRatingTextView)).setText(model.getAgeRating());
            ((TextView) findViewById(R.id.plotTextView)).setText(model.getPlot());
            ((TextView) findViewById(R.id.genreTextView)).setText(model.getGenre());
            ((TextView) findViewById(R.id.releaseTextView)).setText(model.getReleased());
            ((TextView) findViewById(R.id.rateTextView)).setText(model.getRating());
            ((TextView) findViewById(R.id.runtimeTextView)).setText(model.getRuntime());
            Picasso.get().load(model.getImgSrc()).into((ImageView) findViewById(R.id.posterImageView));

            // Storing the original plot
            originalPlot = model.getPlot();
        } else {
            Log.e("MovieDetailsActivity", "Received movie model is null");
        }

        Button favButton = findViewById(R.id.favButton);
        favButton.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null && model != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = currentUser.getUid();

                // Create a map of the movie's details
                Map<String, Object> movieData = new HashMap<>();
                movieData.put("title", model.getTitle());
                movieData.put("ageRating", model.getAgeRating());
                movieData.put("plot", model.getPlot());
                movieData.put("genre", model.getGenre());
                movieData.put("released", model.getReleased());
                movieData.put("rating", model.getRating());
                movieData.put("runtime", model.getRuntime());
                movieData.put("imgSrc", model.getImgSrc());

                // Add the movie to Firestore
                db.collection("user_movies")
                        .document(userId)
                        .collection("movies")
                        .document(model.getTitle()) // Use the title as the document ID
                        .set(movieData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(MovieDetailsActivity.this, model.getTitle() + " added to Firestore!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MovieDetailsActivity.this, "Error adding movie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(MovieDetailsActivity.this, "Error: User not logged in or no movie selected", Toast.LENGTH_SHORT).show();
            }
        });
        ;



        // Set up the "Update" button
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(v -> {
            if (model != null) {
                // Retrieve the updated text from the EditText
                String updatedPlot = ((EditText) findViewById(R.id.plotTextView)).getText().toString();
                model.setPlot(updatedPlot);

                // Save the updated plot to Firestore
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userId = currentUser.getUid();

                    // Update the plot in Firestore
                    db.collection("user_movies")
                            .document(userId)
                            .collection("movies")
                            .document(model.getTitle()) // Use the title as the document ID
                            .update("plot", updatedPlot)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MovieDetailsActivity.this, "Plot updated in Firestore!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MovieDetailsActivity.this, "Error updating plot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MovieDetailsActivity.this, "Error: No movie to update", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the "Revert" button
        // Set up the "Revert" button
        Button revertButton = findViewById(R.id.revertButton);
        revertButton.setOnClickListener(v -> {
            if (model != null) {
                // Revert to the original plot
                model.setPlot(originalPlot);
                ((EditText) findViewById(R.id.plotTextView)).setText(originalPlot);

                // Update the reverted plot in Firestore
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userId = currentUser.getUid();

                    // Update the plot in Firestore with the original value
                    db.collection("user_movies")
                            .document(userId)
                            .collection("movies")
                            .document(model.getTitle()) // Use the title as the document ID
                            .update("plot", originalPlot)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MovieDetailsActivity.this, "Plot reverted in Firestore!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MovieDetailsActivity.this, "Error reverting plot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MovieDetailsActivity.this, "Error: No movie to revert", Toast.LENGTH_SHORT).show();
            }
        });


        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_favourites);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_search) {
                Intent mainIntent = new Intent(MovieDetailsActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(0, 0);
                return true;
            } else if (item.getItemId() == R.id.nav_favourites) {
                Intent favIntent = new Intent(MovieDetailsActivity.this, FavouritesActivity.class);
                startActivity(favIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void saveUpdatedPlot(String title, String newPlot) {
        SharedPreferences sharedPreferences = getSharedPreferences("movie_details", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(title + "_plot", newPlot);
        editor.apply(); // Use apply() instead of commit()
    }

    private void loadSavedPlot() {
        String title = model.getTitle();
        SharedPreferences sharedPreferences = getSharedPreferences("movie_details", MODE_PRIVATE);
        String savedPlot = sharedPreferences.getString(title + "_plot", "");
        if (!savedPlot.isEmpty()) {
            model.setPlot(savedPlot);
            ((EditText) findViewById(R.id.plotTextView)).setText(savedPlot);
        } else {
            // If no saved plot exists, use the original plot
            ((EditText) findViewById(R.id.plotTextView)).setText(originalPlot);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedPlot();
    }
}
