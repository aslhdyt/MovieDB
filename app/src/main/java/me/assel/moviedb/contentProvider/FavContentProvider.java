package me.assel.moviedb.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.HashSet;

import static me.assel.moviedb.contentProvider.Contract.PATH_FAV;


/**
 * Created by assel on 6/19/17.
 */

public class FavContentProvider extends ContentProvider {
    private DBHelper dbHelper;

    public static final int FAV = 100;
    public static final int FAV_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //directory
        uriMatcher.addURI(Contract.AUTHORITY, PATH_FAV, FAV);

        //single item
        uriMatcher.addURI(Contract.AUTHORITY, PATH_FAV + "/#", FAV_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);

        queryBuilder.setTables(DBHelper.TABLE_MOVIE);
        switch (match) {
            case FAV:
                break;
            case FAV_ID:
                queryBuilder.appendWhere(DBHelper.ID
                    + "=" + uri.getLastPathSegment());
                break;
        }
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        Cursor retCursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id = 0;
        switch (match) {
            case FAV:
                id = sqlDB.insert(DBHelper.TABLE_MOVIE, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PATH_FAV + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        int taskDelete = 0;
        switch (match) {
            case FAV:
                taskDelete = sqlDB.delete(DBHelper.TABLE_MOVIE, selection, selectionArgs);
                break;
            case FAV_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    taskDelete = sqlDB.delete(DBHelper.TABLE_MOVIE, DBHelper.ID + "=" + id, null);
                } else {
                    taskDelete = sqlDB.delete(DBHelper.TABLE_MOVIE, DBHelper.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return taskDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        String[] available = {
                DBHelper.ID,
                DBHelper.VOTE_AVERAGE,
                DBHelper.VIDEO,
                DBHelper.VOTE_AVERAGE,
                DBHelper.TITLE,
                DBHelper.POPULARITY,
                DBHelper.POSTER_PATH,
                DBHelper.ORIGINAL_LANGUAGE,
                DBHelper.ORIGINAL_TITLE,
                DBHelper.GENRE_IDS,
                DBHelper.BACKDROP_PATH,
                DBHelper.ADULT,
                DBHelper.OVERVIEW,
                DBHelper.RELEASE_DATE
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}
