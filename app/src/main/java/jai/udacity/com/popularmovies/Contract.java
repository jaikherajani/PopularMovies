package jai.udacity.com.popularmovies;

import android.provider.BaseColumns;

/**
 * Created by jaikh on 12-04-2016.
 */
public class Contract {
    public Contract()
    {}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "pop_mov";
        public static final String TABLE_NAME1 = "mp";
        public static final String TABLE_NAME2 = "mr";
        public static final String MOST_POPULAR = "popularity.desc";
        public static final String HIGHLY_RATED = "vote_count.desc";
        public static final String TITLE = "title";
        public static final int FAV = 0; // 1 for Favorite
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_POSTER = "poster_path";
        public static final String MOVIE_POSTER2 = "backdrop_path";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String PLOT_SYNOPSIS = "overview";
        public static final String MOVIE_ID = "id";
    }
}
