package com.example.a2_nisha8868489.model;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FavouritesModel {

    private static FavouritesModel instance;
    private FirebaseFirestore firestore;
    private String userId;
    private List<Movie> favouriteMovies;  // Local list to store favourite movies

    private FavouritesModel() {
        firestore = FirebaseFirestore.getInstance();
        favouriteMovies = new ArrayList<>();  // Initialize the list

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Log.e("FavouritesModel", "User not authenticated");
        }
    }

    public static FavouritesModel getInstance() {
        if (instance == null) {
            instance = new FavouritesModel();
        }
        return instance;
    }

    public void addToFavourites(Movie movie) {
        if (movie == null || userId == null) {
            Log.e("FavouritesModel", "Invalid input: movie or user ID is null.");
            return;
        }

        // Check if the movie already has an ID, otherwise generate one (optional)
        if (movie.getId() == null || movie.getId().isEmpty()) {
            movie.setId(UUID.randomUUID().toString());  // Generate a unique ID if it's missing
        }

        firestore.collection("users")
                .document(userId)
                .collection("favourites")
                .document(movie.getId()) // Use movie ID here
                .set(movie, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("FavouritesModel", "Movie added to favourites");
                    if (!favouriteMovies.contains(movie)) {
                        favouriteMovies.add(movie);
                    }
                })
                .addOnFailureListener(e -> Log.e("FavouritesModel", "Error adding movie to favourites", e));
    }



    //  method to fetch and update the list of favourite movies from Firestore
    public void loadFavouriteMoviesFromFirestore(OnCompleteListener<QuerySnapshot> listener) {
        if (userId == null) {
            Log.e("FavouritesModel", "User ID is null. Cannot load favourite movies.");
            return;
        }

        firestore.collection("users")
                .document(userId)
                .collection("favourites")
                .get()
                .addOnCompleteListener(listener);
    }

    // Getter for favourite movies list
    public List<Movie> getFavouriteMovies() {
        return favouriteMovies;
    }

    // Method to set the favourite movies (after fetching from Firestore)
    public void setFavouriteMovies(List<Movie> movies) {
        this.favouriteMovies = movies;
    }
}
