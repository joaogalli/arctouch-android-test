package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public interface HomeView {
    void navigateToMovieDetailScreen(Movie movie);

    void showMovies(List<Movie> results);
}
