package com.arctouch.codechallenge.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.detail.MovieDetailActivity;
import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private HomePresenter homePresenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        homePresenter = new HomePresenter(this);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        homePresenter.onViewCreated();
    }

    @Override
    public void showMovies(List<Movie> results) {
        recyclerView.setAdapter(new HomeAdapter(homePresenter, results));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToMovieDetailScreen(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_PARAMETER, movie.id);
        this.startActivity(intent);
    }
}
