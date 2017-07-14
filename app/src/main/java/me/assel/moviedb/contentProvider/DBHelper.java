package me.assel.moviedb.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by assel on 7/6/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_MOVIE = "Movies";

    public static final String ID = "_id";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VIDEO = "video";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String TITLE = "title";
    public static final String POPULARITY= "popularity";
    public static final String POSTER_PATH = "poster_path";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String GENRE_IDS = "genre_ids";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String ADULT = "adult";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MOVIE
            + "("
            + ID + " integer primary key, "
            + VOTE_COUNT + " integer, "
            + VIDEO + " integer, "
            + VOTE_AVERAGE + " real, "
            + TITLE + " text not null, "
            + POPULARITY + " real, "
            + POSTER_PATH + " text, "
            + ORIGINAL_LANGUAGE + " text, "
            + ORIGINAL_TITLE + " text, "
            + GENRE_IDS + " text, "
            + BACKDROP_PATH + " text, "
            + ADULT + " integer, "
            + OVERVIEW + " text, "
            + RELEASE_DATE + " text"
            + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);

    }
}
