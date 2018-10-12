package com.arctouch.codechallenge.detail;

import com.arctouch.codechallenge.model.Movie;

public interface MovieDetailView {
    void showMovie(Movie movie);

    void showErrorLoadingMovie();
}
