package com.arctouch.codechallenge.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailView {

    public static final String MOVIE_ID_PARAMETER = "movieId";

    private MovieDetailPresenter movieDetailPresenter;

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    private TextView titleTextView;
    private TextView genresTextView;
    private TextView releaseDateTextView;
    private ImageView posterImageView;
    private ImageView backdropImageView;
    private TextView overviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra(MovieDetailActivity.MOVIE_ID_PARAMETER, 0);

        if (movieId == 0) {
            showErrorLoadingMovie();
        } else {
            movieDetailPresenter = new MovieDetailPresenter(this);

            titleTextView = this.findViewById(R.id.titleTextView);
            genresTextView = this.findViewById(R.id.genresTextView);
            overviewTextView = this.findViewById(R.id.overviewTextView);
            releaseDateTextView = this.findViewById(R.id.releaseDateTextView);
            posterImageView = this.findViewById(R.id.posterImageView);
            backdropImageView = this.findViewById(R.id.backdropImageView);

            movieDetailPresenter.onViewCreated(movieId);
        }
    }

    @Override
    public void showMovie(Movie movie) {
        titleTextView.setText(movie.title);
        genresTextView.setText(TextUtils.join(", ", movie.genres));
        overviewTextView.setText(movie.overview);
        releaseDateTextView.setText(movie.releaseDate);

        String posterPath = movie.posterPath;
        if (!TextUtils.isEmpty(posterPath)) {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(posterImageView);
        }

        String backdropPath = movie.backdropPath;
        if (!TextUtils.isEmpty(backdropPath)) {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildBackdropUrl(backdropPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(backdropImageView);
        }
    }

    @Override
    public void showErrorLoadingMovie() {
        Toast.makeText(this, R.string.error_loading_movie, Toast.LENGTH_SHORT).show();
        finish();
    }
}
