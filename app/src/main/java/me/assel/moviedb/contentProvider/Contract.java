package me.assel.moviedb.contentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

import java.io.StringWriter;

/**
 * Created by assel on 6/19/17.
 */

public class Contract {
    public static final String AUTHORITY = "me.assel.moviedb.contentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAV = "fav";

    public static final class Entry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

    }
}
