package com.arctouch.codechallenge.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.detail.MovieDetailActivity;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private static final String MOVIES_STATE = "movies_state";

    private HomePresenter homePresenter;
    private RecyclerView recyclerView;
    private EndlessScrollListener endlessScrollListener;
    private HomeAdapter homeAdapter;
    private ProgressBar progressBar;

    private long currentPage = 1l;
    private boolean searching = false;
    private String searchingQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        homePresenter = new HomePresenter(this);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        homeAdapter = new HomeAdapter(homePresenter);

        buildRecyclerView();

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_STATE)) {
            ArrayList<Movie> movies = (ArrayList<Movie>) savedInstanceState.getSerializable(MOVIES_STATE);
            homeAdapter.addMovies(movies);
            progressBar.setVisibility(View.GONE);
        } else {
            homePresenter.onViewCreated();
        }
    }

    private void buildRecyclerView() {
        recyclerView.setAdapter(homeAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void loadMoreItems() {
                setAllowedLoadMoreItems(false);

                progressBar.setVisibility(View.VISIBLE);
                if (searching)
                    homePresenter.searchMovies(searchingQuery, currentPage);
                else
                    homePresenter.loadUpcomingMovies(currentPage);
            }
        };
        recyclerView.setOnScrollListener(endlessScrollListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Movie> movies = homeAdapter.getSerializableMovies();
        if (movies != null)
            outState.putSerializable(MOVIES_STATE, movies);
    }

    @Override
    public void addMoviesPage(List<Movie> results) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem menuSearch = menu.findItem(R.id.search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menuSearch.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        menuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                showUpcomingMovies();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newSearchMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            showUpcomingMovies();
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void newSearchMovies(String query) {
        progressBar.setVisibility(View.VISIBLE);
        searchingQuery = query;
        searching = true;
        resetAdapter();
        homePresenter.searchMovies(query, currentPage);
    }

    private void showUpcomingMovies() {
        progressBar.setVisibility(View.VISIBLE);

        searching = false;
        resetAdapter();
        homePresenter.loadUpcomingMovies(currentPage);
    }

    private void resetAdapter() {
        currentPage = 1;
        homeAdapter.clearMovies();
    }

    public void showErrorMessage(int message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
