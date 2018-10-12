package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BasePresenter;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends BasePresenter {

    private HomeView homeView;

    public HomePresenter(HomeView view) {
        this.homeView = view;
    }

    public void onViewCreated() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                });

        loadUpcomingMovies(1L);
    }

    public void movieClicked(Movie movie) {
        homeView.navigateToMovieDetailScreen(movie);
    }

    public void loadUpcomingMovies(long page) {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (Movie movie : response.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }

                    homeView.addMoviesPage(response.results);
                }, throwable -> {
                    homeView.showErrorMessage(R.string.errorLoadingMovies);
                });
    }

    public void searchMovies(String query, long page) {
        api.searchMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, query, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (Movie movie : response.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }

                    homeView.addMoviesPage(response.results);
                }, throwable -> {
                    homeView.showErrorMessage(R.string.errorLoadingMovies);
                });
    }


}
