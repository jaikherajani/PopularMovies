package jai.udacity.com.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static jai.udacity.com.popularmovies.R.*;

public class MovieDetail extends AppCompatActivity {
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String MOVIE_POSTER2= "backdrop_path";
    private final String VOTE_AVERAGE = "vote_average";
    private final String PLOT_SYNOPSIS = "overview";
    private final String MOVIE_ID = "id";
    public String[] review = new String[5];
    public String[] author = new String[5];
    private String resultJSON = null;
    public TextView plotView,voteAvg, releaseDate, Title, reviews;
    public ImageView imageView,imageView2,trailerview;
    public String title, release, poster,poster2, vote, plot,trailer,key,fav;
    public String id;
    public Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.movie_detail);
        Intent intent = getIntent();
        title = intent.getStringExtra(TITLE);
        release = intent.getStringExtra(RELEASE_DATE);
        poster = intent.getStringExtra(MOVIE_POSTER);
        poster2 = intent.getStringExtra(MOVIE_POSTER2);
        vote = intent.getStringExtra(VOTE_AVERAGE);
        plot = intent.getStringExtra(PLOT_SYNOPSIS);
        fav= intent.getStringExtra("fav");
        poster = "http://image.tmdb.org/t/p/w185/" + poster;
        poster2 = "http://image.tmdb.org/t/p/w780/" + poster2;
        id=intent.getStringExtra(MOVIE_ID);
            Bundle args = new Bundle();
            args.putString("Title", title);
        args.putString("Poster", poster);
        args.putString("Poster2", poster2);
        args.putString("release_date", release);
        args.putString("vote", vote);
        args.putString("plot", plot);
        args.putString("movie_id",id);
        args.putString("fav",fav);
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        detailFragment.setArguments(args);
        setContentView(layout.activity_movie_detail);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerDetails, detailFragment).commit();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
