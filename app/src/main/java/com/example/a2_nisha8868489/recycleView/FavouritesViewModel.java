package com.example.a2_nisha8868489.recycleView;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a2_nisha8868489.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavouritesViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> favouriteMoviesLiveData;
    private FirebaseFirestore firestore;
    private String userId;

    public FavouritesViewModel() {
        favouriteMoviesLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMoviesLiveData;
    }

    public void fetchFavouriteMovies() {
        firestore.collection("user_movies")
                .document(userId)
                .collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Movie> movies = task.getResult().toObjects(Movie.class);
                        favouriteMoviesLiveData.setValue(movies);
                    } else {
                        Log.e("FavouritesViewModel", "Error fetching movies: " + task.getException().getMessage());
                    }
                });
    }

    public void deleteFavMovieByTitle(String title, String userId) {
        if (title == null || title.isEmpty()) {
            Log.e("FavouritesViewModel", "Movie title is null or empty. Cannot delete.");
            return; // Prevent delete operation if the title is invalid
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Search for the document in the "movies" collection where the title matches
        db.collection("user_movies")
                .document(userId)
                .collection("movies")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Get the document ID of the first matching document
                        String documentId = querySnapshot.getDocuments().get(0).getId();

                        // Delete the document
                        db.collection("user_movies")
                                .document(userId)
                                .collection("movies")
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("FavouritesViewModel", "Movie deleted successfully");
                                    fetchFavouriteMovies(); // Refresh the list after deletion
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FavouritesViewModel", "Error deleting movie", e);
                                });
                    } else {
                        Log.e("FavouritesViewModel", "No movie found with the given title.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FavouritesViewModel", "Error finding movie by title", e);
                });
    }


    public String getUserId() {
        return userId;
    }
}