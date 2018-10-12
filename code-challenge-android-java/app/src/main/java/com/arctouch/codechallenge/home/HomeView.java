package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public interface HomeView {
    void navigateToMovieDetailScreen(Movie movie);

    void addMoviesPage(List<Movie> results);

    void showErrorMessage(int message);
}
