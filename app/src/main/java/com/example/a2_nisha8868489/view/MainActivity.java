package com.example.a2_nisha8868489.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.a2_nisha8868489.R;
import com.example.a2_nisha8868489.ViewModel.SearchViewModel;
import com.example.a2_nisha8868489.databinding.ActivityMainBinding;
import com.example.a2_nisha8868489.model.Movie;
import com.example.a2_nisha8868489.recycleView.MovieAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchViewModel viewModel; // ViewModel for handling movie data
    private ActivityMainBinding binding; // View binding for accessing layout elements
    private MovieAdapter adapter; // Adapter for the RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the RecyclerView
        adapter = new MovieAdapter(this, Collections.emptyList()); // Initialize with an empty list
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use a vertical LinearLayoutManager
        binding.recyclerView.setAdapter(adapter); // Set the adapter to the RecyclerView

        // Observe the movie results from the ViewModel
        viewModel.getMovieResults().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movieList) {
                adapter.updateList(movieList); // Update the adapter with the new movie list
            }
        });

        // Observe error messages from the ViewModel (optional)
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            // Handle error messages (e.g., show a Toast or an AlertDialog)
            // Example: Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        });


        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_favourites); // Set the default selected item as Favourites

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_search) {
                // Navigate to MainActivity (Search page)
                Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(0, 0); // Smooth transition
                return true;

            } else if (item.getItemId() == R.id.nav_favourites) {
                // Navigate to FavouritesActivity (Favourites page)
                Intent favIntent = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivity(favIntent);
                overridePendingTransition(0, 0); // Smooth transition
                return true;
            }

            return false;
        });



        // Set up search button click listener
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = binding.searchInput.getText().toString().trim(); // Get the search query
                viewModel.fetchMovies(query); // Call fetchMovies in ViewModel
            }});



    }
}