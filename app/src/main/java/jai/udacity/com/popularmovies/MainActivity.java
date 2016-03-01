package jai.udacity.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private final String MOST_POPULAR = "popularity.desc";
    private final String HIGHLY_RATED = "vote_count.desc";
    private final String TITLE = "title";
    private final String RELEASE_DATE = "release_date";
    private final String MOVIE_POSTER = "poster_path";
    private final String VOTE_AVERAGE = "vote_average";
    private final String PLOT_SYNOPSIS = "overview";
    private GridView gridView;
    private String resultJSON = null;
    private String[] imgUrl = new String[20];
    String status="popularity.desc";
    View myview;
    //private AlertDialog choice;
    //private String FLAG_CURRENT = MOST_POPULAR;
    private JSONArray movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        decide();
        myview=(View)findViewById(R.id.fragment);
        Context context = getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share functionality will be added in Stage 2", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popularity) {
            status = "popularity.desc";
            sort(status);
            Snackbar.make(myview, "Sorted according to Popularity", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if(id == R.id.action_ratings) {
            status="vote_count.desc";
            sort(status);
            Snackbar.make(myview, "Sorted according to Ratings", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void decide()
    {
        boolean abc=isNetworkAvailable();
        if(abc)
        {
            sort(status);
        }
        else
        {
            Snackbar.make(findViewById(R.id.fragment), "No Internet Connectivity !",Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void sort(String status)
    {
            if (FetchMovie()) {
                gridView = (GridView) findViewById(R.id.gridview);
                gridView.setAdapter(new ImageAdapter(this, imgUrl));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            JSONObject object = movieDetails.getJSONObject(position);
                            String title = object.getString(TITLE);
                            String poster = "" + object.getString(MOVIE_POSTER);
                            String release_date = object.getString(RELEASE_DATE);
                            String vote = object.getString(VOTE_AVERAGE);
                            String plot = object.getString(PLOT_SYNOPSIS);

                            Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
                            intent.putExtra(TITLE, title);
                            intent.putExtra(MOVIE_POSTER, poster);
                            intent.putExtra(RELEASE_DATE, release_date);
                            intent.putExtra(VOTE_AVERAGE, vote);
                            intent.putExtra(PLOT_SYNOPSIS, plot);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(view, "Error Code : " + e, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }
                });
            } else {
                Snackbar.make(myview, "Some Error Happened !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
    }

    private boolean FetchMovie() {
        Fetchdata task=new Fetchdata();

        try {
            resultJSON = task.execute(status).get();
            if (resultJSON != null) {
                JSONObject movie = new JSONObject(resultJSON);
                movieDetails = movie.getJSONArray("results");
                for (int i = 0; i < movieDetails.length(); i++) {
                    JSONObject temp_mov_poster = movieDetails.getJSONObject(i);
                    imgUrl[i] = "http://image.tmdb.org/t/p/w185/" + temp_mov_poster.getString("poster_path");
                }
                return true;
            } else
                return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}
