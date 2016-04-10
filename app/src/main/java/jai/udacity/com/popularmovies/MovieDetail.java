package jai.udacity.com.popularmovies;


import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by jaikh on 27-02-2016.
 */
public class MovieDetail extends AppCompatActivity {
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String MOVIE_POSTER2= "backdrop_path";
    private final String VOTE_AVERAGE = "vote_average";
    private final String PLOT_SYNOPSIS = "overview";
    private final String MOVIE_ID = "id";
    private String[] review = new String[5];
    private String[] author = new String[5];
    private String resultJSON = null;
    private TextView plotView,voteAvg, releaseDate, Title, reviews;
    private ImageView imageView,imageView2,trailerview;
    private String title, release, poster,poster2, vote, plot,trailer,key;
    public static String id;
    private Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       plotView = (TextView) findViewById(R.id.synopsis);
       voteAvg = (TextView) findViewById(R.id.vote_average);
        releaseDate = (TextView) findViewById(R.id.release_date);
        Title = (TextView) findViewById(R.id.name);
        imageView = (ImageView) findViewById(R.id.big_poster);
        imageView2 = (ImageView) findViewById(R.id.small_poster);
        trailerview = (ImageView)findViewById(R.id.moviestills);
        reviews = (TextView) findViewById(R.id.reviews);
        UIUpdate();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UIUpdate() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(TITLE) && extras.containsKey(MOVIE_ID) && extras.containsKey(RELEASE_DATE) && extras.containsKey(MOVIE_POSTER) && extras.containsKey(VOTE_AVERAGE) && extras.containsKey(PLOT_SYNOPSIS)) {
                title = intent.getStringExtra(TITLE);
                release = intent.getStringExtra(RELEASE_DATE);
                poster = intent.getStringExtra(MOVIE_POSTER);
                poster2 = intent.getStringExtra(MOVIE_POSTER2);
                vote = intent.getStringExtra(VOTE_AVERAGE);
                plot = intent.getStringExtra(PLOT_SYNOPSIS);
                poster = "http://image.tmdb.org/t/p/w185/" + poster;
                poster2 = "http://image.tmdb.org/t/p/w780/" + poster2;
                id=intent.getStringExtra(MOVIE_ID);
            }
        }
        plotView.setText(plot);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(release);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        releaseDate.setText(df.format(date).toString());
        Title.setText(title);
        myToolbar.setTitle(title);
        View myview=(View)findViewById(R.id.moviedetail);
        Snackbar.make(myview, "" + title, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        voteAvg.setText("TMDb : "+vote);
        imageView2.bringToFront();
        Picasso.with(this)
                .load(poster2)
                .into(imageView);
        Picasso.with(this)
                .load(poster)
                .into(imageView2);

        Fetchdata task = new Fetchdata();
        try {
            resultJSON = task.execute("videos").get();
            if (resultJSON != null) {
                JSONObject movie = new JSONObject(resultJSON);
                JSONArray movieDetails = movie.getJSONArray("results");
                for (int i = 0; i < movieDetails.length(); i++) {
                    JSONObject mov_trailer = movieDetails.getJSONObject(i);
                    key = mov_trailer.getString("key");
                }
            }
        }
         catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        trailer="http://img.youtube.com/vi/"+key+"/hqdefault.jpg";
        Picasso.with(this)
                .load(trailer)
                .into(trailerview);

        Fetchdata task1 = new Fetchdata();
        reviews.setText("");
        try {
            resultJSON = task1.execute("reviews").get();
            if (resultJSON != null) {
                JSONObject movie = new JSONObject(resultJSON);
                JSONArray movieDetails = movie.getJSONArray("results");
                for (int i = 0; i <=5; i++) {
                    JSONObject mov_reviews = movieDetails.getJSONObject(i);
                    review[i] = mov_reviews.getString("author");
                    author[i] = mov_reviews.getString("content");
                    reviews.append(review[i]+" - "+author[i]);
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void playtrailer(View v) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v="+key));
        startActivity(intent1);
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
