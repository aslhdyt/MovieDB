package me.assel.moviedb.presenter;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import me.assel.moviedb.contentProvider.Contract;
import me.assel.moviedb.model.Movies;
import me.assel.moviedb.presenter.adapter.MovieAdapter;

import static me.assel.moviedb.contentProvider.DBHelper.ADULT;
import static me.assel.moviedb.contentProvider.DBHelper.BACKDROP_PATH;
import static me.assel.moviedb.contentProvider.DBHelper.GENRE_IDS;
import static me.assel.moviedb.contentProvider.DBHelper.ID;
import static me.assel.moviedb.contentProvider.DBHelper.ORIGINAL_LANGUAGE;
import static me.assel.moviedb.contentProvider.DBHelper.ORIGINAL_TITLE;
import static me.assel.moviedb.contentProvider.DBHelper.OVERVIEW;
import static me.assel.moviedb.contentProvider.DBHelper.POPULARITY;
import static me.assel.moviedb.contentProvider.DBHelper.POSTER_PATH;
import static me.assel.moviedb.contentProvider.DBHelper.RELEASE_DATE;
import static me.assel.moviedb.contentProvider.DBHelper.TITLE;
import static me.assel.moviedb.contentProvider.DBHelper.VIDEO;
import static me.assel.moviedb.contentProvider.DBHelper.VOTE_AVERAGE;
import static me.assel.moviedb.contentProvider.DBHelper.VOTE_COUNT;

/**
 * Created by assel on 5/28/17.
 */

public class FromDBPresenter implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;
    private Bundle saveInstance;

    private SwipeRefreshLayout swipeRefresh;

    private int TASK_LOADER_ID = 0;
    private AppCompatActivity mActivity;


    public FromDBPresenter(AppCompatActivity activity, RecyclerView mRecyclerView, Bundle savedInstanceState, SwipeRefreshLayout refreshLayout) {
        mActivity = activity;
        recyclerView = mRecyclerView;
        mLayoutManager = new GridLayoutManager(mActivity, 2);
        swipeRefresh = refreshLayout;
        saveInstance = savedInstanceState;

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadData();
    }

    public void loadData() {
        mActivity.getLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }


    public void setColumn(int column) {
        mLayoutManager.setSpanCount(column);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(mActivity) {
            Cursor mTaskData = null;
            @Override
            protected void onStartLoading() {
                swipeRefresh.setRefreshing(true);
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
                    return mActivity.getContentResolver().query(Contract.Entry.CONTENT_URI,
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
        List<Movies> moviesArrayList = new ArrayList<>();
        if (data.moveToFirst()) {
            do {
                //reconvert from cursor to Movies object
                Movies movies = new Movies();
                movies.setId(data.getInt(data.getColumnIndex(ID)));
                movies.setVoteCount(data.getInt(data.getColumnIndex(VOTE_COUNT)));
                movies.setVideo(data.getString(data.getColumnIndex(VIDEO)).equals("true"));
                movies.setVoteAverage(data.getFloat(data.getColumnIndex(VOTE_AVERAGE)));
                movies.setTitle(data.getString(data.getColumnIndex(TITLE)));
                movies.setPopularity(data.getFloat(data.getColumnIndex(POPULARITY)));
                movies.setPosterPath(data.getString(data.getColumnIndex(POSTER_PATH)));
                movies.setOriginalLanguage(data.getString(data.getColumnIndex(ORIGINAL_LANGUAGE)));
                movies.setOriginalTitle(data.getString(data.getColumnIndex(ORIGINAL_TITLE)));
                String CSV = data.getString(data.getColumnIndex(GENRE_IDS));
                movies.setGenreIds(toIntArray(CSV));
                movies.setBackdropPath(data.getString(data.getColumnIndex(BACKDROP_PATH)));
                movies.setAdult(data.getString(data.getColumnIndex(ADULT)).equals("true"));
                movies.setOverview(data.getString(data.getColumnIndex(OVERVIEW)));
                movies.setReleaseDate(data.getString(data.getColumnIndex(RELEASE_DATE)));

                moviesArrayList.add(movies);
            } while (data.moveToNext());
        }
        MovieAdapter adapter = new MovieAdapter(mActivity, moviesArrayList);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
        if (saveInstance != null) {
            mLayoutManager.onRestoreInstanceState(saveInstance.getParcelable("layoutManager"));
        }
    }

    private int[] toIntArray(String csv) {
        String[] strArr = csv.split(",");
        int[] intArr = new int[strArr.length];
        int i = 0;
        for (String s : strArr) {
            intArr[i] = Integer.parseInt(s);
            i++;
        }
        return intArr;
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.setAdapter(null);
    }

    public void onResume(AppCompatActivity activity) {
        activity.getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    public void saveInstance(Bundle outState) {
        outState.putParcelable("layoutManager", mLayoutManager.onSaveInstanceState());

    }
}
