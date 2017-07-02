package me.assel.moviedb.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.assel.moviedb.contentProvider.Contract;
import me.assel.moviedb.model.Movies;
import me.assel.moviedb.presenter.adapter.MovieAdapter;

/**
 * Created by assel on 5/28/17.
 */

public class FromDBPresenter implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;

    private MovieAdapter adapter;

    SwipeRefreshLayout swipeRefresh;

    int TASK_LOADER_ID = 0;
    private Activity mActivity;


    public FromDBPresenter(Activity activity, RecyclerView mRecyclerView, Bundle savedInstanceState, SwipeRefreshLayout refreshLayout) {
        mActivity = activity;
        recyclerView = mRecyclerView;
        mLayoutManager = new GridLayoutManager(mActivity, 2);
        swipeRefresh = refreshLayout;

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
        List<Movies> moviesList = new ArrayList<Movies>();
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
                movies.setBackdropPath(data.getString(data.getColumnIndex("backdrop_path")));
                movies.setAdult(data.getString(data.getColumnIndex("adult")).equals("true"));
                movies.setOverview(data.getString(data.getColumnIndex("overview")));
                movies.setReleaseDate(data.getString(data.getColumnIndex("release_date")));

                moviesList.add(movies);
            } while (data.moveToNext());
        }
        adapter = new MovieAdapter(mActivity, moviesList);
        recyclerView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerView.setAdapter(null);
    }

    public void onResume(Activity activity) {
        activity.getLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }
}
