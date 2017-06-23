package me.assel.moviedb.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import me.assel.moviedb.AppConfig;
import me.assel.moviedb.api.response.Movies;


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
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Realm realm = Realm.getInstance(AppConfig.realmConfig());
        int match = sUriMatcher.match(uri);

        Gson gson =  new Gson();
        Movies movies = gson.fromJson(values.getAsString("json"), Movies.class);

        Uri returnUri;
        switch (match) {
            case FAV:
                realm.beginTransaction();
                Movies realmTransaction= realm.copyToRealm(movies);
                long id = realmTransaction.getId();
                realm.commitTransaction();
                Log.d("Realm", "id = "+id);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(Contract.Entry.CONTENT_URI, id);
                    Log.d("Provider", "retrunUri = "+returnUri);
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
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
