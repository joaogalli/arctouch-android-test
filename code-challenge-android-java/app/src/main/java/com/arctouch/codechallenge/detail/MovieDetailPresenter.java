package com.arctouch.codechallenge.detail;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailPresenter extends BasePresenter {

    private MovieDetailView movieDetailView;

    public MovieDetailPresenter(MovieDetailView movieDetailView) {
        this.movieDetailView = movieDetailView;
    }

    public void onViewCreated(int movieId) {
        Long id = new Long(movieId);

        api.movie(id, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    movieDetailView.showMovie(response);
                }, throwable -> {
                    movieDetailView.showErrorLoadingMovie();
                });
    }
}
