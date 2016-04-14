package jai.udacity.com.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fetchdata extends AsyncTask<String, Void, String> {
    public static String apiKey="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private final String LOG_TAG = Fetchdata.class.getSimpleName();
    URL url;
    String movie_id=MovieDetail.id;

    @Override
    protected String doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String MovieDBjson = null;
        String parameter = params[0];

        try {
            // Construct the URL for the theMoviedb query
            // Possible parameters are avaiable at TMDB's API page, at
            // http://api.themoviedb.org/3/discover/movie?api_key=[API KEY]
            if(parameter == "videos")
                url = new URL("http://api.themoviedb.org/3/movie/"+movie_id+"/videos?&api_key=" + apiKey);
            else if (parameter=="reviews")
                url = new URL("http://api.themoviedb.org/3/movie/"+movie_id+"/reviews?&api_key=" + apiKey);
            else
            url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=" + parameter + "&api_key=" + apiKey);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            MovieDBjson = buffer.toString();
            Log.d("result", MovieDBjson);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return MovieDBjson;
    }
}
