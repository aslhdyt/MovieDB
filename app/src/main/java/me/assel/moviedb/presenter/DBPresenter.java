package me.assel.moviedb.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.assel.moviedb.adapter.DbAdapter;
import me.assel.moviedb.api.response.Movies;
import me.assel.moviedb.contentProvider.Contract;

/**
 * Created by assel on 5/28/17.
 */

public class DBPresenter implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;

    private DbAdapter adapter;

    int TASK_LOADER_ID = 0;


    private static final String STATE_MOVIE = "movies_state";
    private int PAGE_SIZE = 0;
    private int CUR_PAGE = 0;

    public DBPresenter(Activity activity, RecyclerView mRecyclerView, Bundle savedInstanceState) {
        context = activity;
        recyclerView = mRecyclerView;
        mLayoutManager = new GridLayoutManager(context, 2);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        Realm realm = Realm.getInstance(realmConfig());
//        realm.beginTransaction();
//        RealmResults<DbObject> list = realm.where(DbObject.class)
//                .findAll();
//        realm.commitTransaction();
    }


    public void setColumn(int column) {
        mLayoutManager.setSpanCount(column);
    }

    public void refresh() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(context) {
            Cursor mTaskData = null;
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return context.getContentResolver().query(Contract.Entry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                } catch (Exception e) {
                    Log.e("LOADER", "Failed  to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List moviesList = new ArrayList();
        if (data.moveToFirst()) {
            do {
                //reconvert from cursor to Movies object
                Movies movies = new Movies();
                movies.setId(data.getInt(data.getColumnIndex("id")));
                movies.setVoteCount(data.getInt(data.getColumnIndex("vote_count")));
                movies.setVideo(data.getString(data.getColumnIndex("video")).equals("true"));
                movies.setVoteAverage(data.getFloat(data.getColumnIndex("vote_average")));
                movies.setTitle(data.getString(data.getColumnIndex("title")));
                movies.setPopularity(data.getFloat(data.getColumnIndex("popularity")));
                movies.setPosterPath(data.getString(data.getColumnIndex("poster_path")));
                movies.setOriginalLanguage(data.getString(data.getColumnIndex("original_language")));
                movies.setOriginalTitle(data.getString(data.getColumnIndex("original_title")));
//                movies.setGenreIds(data)

            } while (data.moveToNext());
        }
        adapter = new DbAdapter((Activity) context, moviesList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onResume(Activity activity) {
        activity.getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }
}
