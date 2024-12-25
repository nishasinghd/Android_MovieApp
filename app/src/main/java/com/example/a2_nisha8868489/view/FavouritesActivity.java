package com.example.a2_nisha8868489.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2_nisha8868489.R;
import com.example.a2_nisha8868489.model.Movie;
import com.example.a2_nisha8868489.recycleView.FavouritesAdapter;
import com.example.a2_nisha8868489.recycleView.FavouritesViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView favouritesRecyclerView;
    private FavouritesAdapter favouritesAdapter;
    private FavouritesViewModel favouritesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        // Initialize RecyclerView
        favouritesRecyclerView = findViewById(R.id.favouritesRecyclerView);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        favouritesViewModel = new FavouritesViewModel();

        // Initialize the adapter
        favouritesAdapter = new FavouritesAdapter(this, favouritesViewModel);
        favouritesRecyclerView.setAdapter(favouritesAdapter);

        // BottomNavigationView setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_favourites);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_search) {
                Intent mainIntent = new Intent(FavouritesActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        // Observe LiveData from ViewModel
        favouritesViewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                // Update the adapter when LiveData changes
                favouritesAdapter.setFavouriteMovies(movies);
            }
        });

        // Load favourite movies from Firestore
        favouritesViewModel.fetchFavouriteMovies();
    }
}