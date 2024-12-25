package com.example.a2_nisha8868489.model;

import java.io.Serializable;
import java.util.Objects;

public class Movie implements Serializable {
    private String id; // Firestore document ID or unique movie identifier
    private String title;
    private String year;
    private String imgSrc;
    private String imdbId;
    private String rating;
    private String plot;
    private String ageRating;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String language;

    // Default constructor required for Firestore
    public Movie() {
    }

    // Parameterized constructor
    public Movie(String id, String title, String year, String imgSrc, String imdbId, String rating,
                 String plot, String ageRating, String released, String runtime,
                 String genre, String director, String language) {
        this.id = id; // Ensure this is unique (e.g., use the IMDb ID or a generated UUID)
        this.title = title;
        this.year = year;
        this.imgSrc = imgSrc;
        this.imdbId = imdbId;
        this.rating = rating;
        this.plot = plot;
        this.ageRating = ageRating;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.language = language;
    }

    public Movie(String title, String ageRating, String plot, String genre, String released, String rating, String runtime, String imgSrc) {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    // Equality based on `id`
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id); // Use `id` for comparison
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Use `id` for hashing
    }

    // Improved toString for debugging
    @Override
    public String toString() {
        return String.format("Movie{id='%s', title='%s', year='%s', rating='%s', genre='%s'}",
                id, title, year, rating, genre);
    }
}
