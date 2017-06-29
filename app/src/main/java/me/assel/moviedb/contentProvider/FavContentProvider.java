package me.assel.moviedb.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import me.assel.moviedb.AppConfig;
import me.assel.moviedb.model.Movies;

import static me.assel.moviedb.AppConfig.realmConfig;


/**
 * Created by assel on 6/19/17.
 */

public class FavContentProvider extends ContentProvider {
    public static final int FAV = 100;
    public static final int FAV_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //directory
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAV, FAV);

        //single item
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAV + "/#", FAV_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Realm realm  = Realm.getInstance(realmConfig());
        int match = sUriMatcher.match(uri);
        // TODO: 6/23/17 find solution for Realm to Cursor 
        MatrixCursor retCursor = null;
        //because realm doesnt support convert to Cursor object, i need to make custom Cursor column
        String[] column = {"id", "vote_count", "video", "vote_average", "title", "popularity",
                "poster_path", "original_language", "original_title", "genre_ids", "backdrop_path",
                "adult", "overview", "release_date"};
        switch (match) {
            case FAV:
                
                realm.beginTransaction();
                RealmResults<Movies> list = realm.where(Movies.class).findAll();
                realm.commitTransaction();

                retCursor = new MatrixCursor(column);
                for (Movies movie : list) {
                    Object[] rowData = {
                            movie.getId(), movie.getVoteCount(), movie.isVideo(), movie.getVoteAverage(),
                            movie.getTitle(), movie.getPopularity(), movie.getPosterPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(),
                            movie.getGenreIds(), movie.getBackdropPath(), movie.isAdult(), movie.getOverview(), movie.getReleaseDate()
                    };
                    retCursor.addRow(rowData);
                    retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
                break;
            case FAV_ID:
                final int id = Integer.parseInt(uri.getPathSegments().get(1));

                realm.beginTransaction();
                Movies movie = realm.where(Movies.class).equalTo("id", id).findFirst();
                if (movie != null) {
                    retCursor = new MatrixCursor(column);
                    Object[] rowData = {
                            movie.getId(), movie.getVoteCount(), movie.isVideo(), movie.getVoteAverage(),
                            movie.getTitle(), movie.getPopularity(), movie.getPosterPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(),
                            movie.getGenreIds(), movie.getBackdropPath(), movie.isAdult(), movie.getOverview(), movie.getReleaseDate()
                    };
                    retCursor.addRow(rowData);
                    retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                }
                realm.commitTransaction();
                break;

        }
        realm.close();
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
        Realm realm = Realm.getInstance(realmConfig());
        int match = sUriMatcher.match(uri);
        Log.d("INSERT", "URI = "+uri);

        Gson gson = AppConfig.gsonBuilder();
        Movies movies = gson.fromJson(values.getAsString(Contract.Entry.JSON), new TypeToken<Movies>(){}.getType());

        Log.d("MOVIES", "genreIDs = "+movies.getGenreIds());

        Uri returnUri;
        switch (match) {
            case FAV:
                realm.beginTransaction();
                Movies realmTransaction= realm.copyToRealmOrUpdate(movies);
                long id = realmTransaction.getId();
                realm.commitTransaction();
                realm.close();
                Log.d("Realm", "id = "+id);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract.Entry.CONTENT_URI, id);
                    Log.d("Provider", "returnUri = "+returnUri);
                } else {
                    throw new RealmException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Realm realm = Realm.getInstance(realmConfig());
        int match = sUriMatcher.match(uri);

        int taskDelete = 0;
        switch (match) {
            case FAV_ID:
                final int id = Integer.parseInt(uri.getPathSegments().get(1));

                realm.beginTransaction();

                Movies query = realm.where(Movies.class).equalTo("id", id).findFirst();
                if (RealmObject.isValid(query)) {
                    Log.d("DELETE", "movies to delete = "+query.getTitle());
                    RealmObject.deleteFromRealm(query);
                    taskDelete = 1;
                    getContext().getContentResolver().notifyChange(uri, null);
                }else {
                    Log.d("DELETE", "unsuccessful");
                }

                realm.commitTransaction();

                realm.close();


        }
        return taskDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
