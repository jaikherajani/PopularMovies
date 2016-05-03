package jai.udacity.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by jaikh on 17-04-2016.
 */
public class MovieDetailFragment extends Fragment {
    public String[] review = new String[5];
    public String[] author = new String[5];
    private String resultJSON = null;
    public TextView plotView,voteAvg, releaseDate, Title, reviews;
    public ImageView imageView,imageView2,trailerview;
    public String title, release, poster,poster2, vote, plot,trailer;
    public static String key;
    private DBHelper databaseHelper;
    public static String id;
    public Toolbar myToolbar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.movie_detail, container, false);
        databaseHelper = new DBHelper(getContext());
        Title =(TextView)rootView.findViewById(R.id.name);
        plotView = (TextView) rootView.findViewById(R.id.synopsis);
        voteAvg = (TextView) rootView.findViewById(R.id.vote_average);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        Title = (TextView) rootView.findViewById(R.id.name);
        imageView = (ImageView) rootView.findViewById(R.id.big_poster);
        imageView2 = (ImageView) rootView.findViewById(R.id.small_poster);
        trailerview = (ImageView) rootView.findViewById(R.id.moviestills);
        reviews = (TextView) rootView.findViewById(R.id.reviews);
        if (getArguments() != null) {
            title = getArguments().getString("Title");
            poster = getArguments().getString("Poster");
            poster2 = getArguments().getString("Poster2");
            release = getArguments().getString("release_date");
            vote = getArguments().getString("vote");
            id = getArguments().getString("movie_id");
            plot = getArguments().getString("plot");
            Title.setText(title);
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
            voteAvg.setText("TMDb : "+vote);
            imageView2.bringToFront();
            plotView.setText(plot);

           final FloatingActionButton share = (FloatingActionButton) rootView.findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this movie, its awesome. Here's the link - https://www.themoviedb.org/movie/"+id);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent,"Share Movie with..." ));
                }
            });
            final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean result = databaseHelper.insertData(id.toString());
                    if(result) {
                        Snackbar.make(view, "Saved as Favorite", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite));
                    }
                    else
                    {
                        Snackbar.make(view, "Something happended ! This movie might already be saved as favorite !", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
            if(!MainActivity.tablet) {
                myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
                ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            CollapsingToolbarLayout appbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
            appbar.setTitle(title);
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

            final FloatingActionButton viewtrailer = (FloatingActionButton) rootView.findViewById(R.id.trailer);
            viewtrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v="+MovieDetailFragment.key));
                    startActivity(intent1);
                }
            });

            Fetchdata task1 = new Fetchdata();
            reviews.setText("");
            try {
                resultJSON = task1.execute("reviews").get();
                if (resultJSON != null) {
                    JSONObject movie = new JSONObject(resultJSON);
                    JSONArray movieDetails = movie.getJSONArray("results");
                    for (int i = 0; i <=5; i++) {
                        JSONObject mov_reviews = movieDetails.getJSONObject(i);
                        author[i] = mov_reviews.getString("author");
                        review[i] = mov_reviews.getString("content");
                        reviews.append("\nReview By - "+author[i]+"\n"+review[i]+"\n");
                        reviews.append("------------------------------------------------------------------------------------\n");
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
            loadimages();
        }

        return rootView;
    }
    public void loadimages()
    {
        Picasso.with(getContext())
               .load("http://image.tmdb.org/t/p/w780/" + poster2)
               .into(imageView);
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/" + poster)
                .into(imageView2);
        Picasso.with(getContext())
                .load(trailer)
                .into(trailerview);
    }

    public MovieDetailFragment() {

    }


}
