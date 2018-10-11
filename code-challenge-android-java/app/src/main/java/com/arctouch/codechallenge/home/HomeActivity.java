package com.arctouch.codechallenge.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.detail.MovieDetailActivity;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.EndlessScrollListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private HomePresenter homePresenter;
    private RecyclerView recyclerView;
    private EndlessScrollListener endlessScrollListener;
    private HomeAdapter homeAdapter;
    private ProgressBar progressBar;

    private long currentPage = 0l;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        homePresenter = new HomePresenter(this);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        homeAdapter = new HomeAdapter(homePresenter);

        buildRecyclerView();

        homePresenter.onViewCreated();
    }

    private void buildRecyclerView() {
        recyclerView.setAdapter(homeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void loadMoreItems() {
                setAllowedLoadMoreItems(false);
                homePresenter.loadPage(currentPage + 1);
            }
        };
        recyclerView.setOnScrollListener(endlessScrollListener);
    }

    @Override
    public void showMovies(List<Movie> results) {
        currentPage++;

        boolean isLastPage = results.isEmpty();
        endlessScrollListener.setAllowedLoadMoreItems(!isLastPage);

        homeAdapter.addMovies(results);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void navigateToMovieDetailScreen(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_PARAMETER, movie.id);
        this.startActivity(intent);
    }
}
