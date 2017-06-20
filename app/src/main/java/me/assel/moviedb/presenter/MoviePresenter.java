package me.assel.moviedb.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.assel.moviedb.R;
import me.assel.moviedb.adapter.MovieAdapter;
import me.assel.moviedb.api.request.RequestInterface;
import me.assel.moviedb.api.response.Movies;
import me.assel.moviedb.api.response.Page;
import me.assel.moviedb.api.utils.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static me.assel.moviedb.AppConfig.API_KEY;
import static me.assel.moviedb.AppConfig.retrofitBuilder;

/**
 * Created by assel on 5/23/17.
 */

public class MoviePresenter {
    private static final String STATE_MOVIE = "movies_state";
    private String TAG = "presenter";


    private Context context;
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;

    private final RequestInterface request;

    private List<Movies> allResult = new ArrayList<>();
    private MovieAdapter adapter;
    private int PAGE_SIZE = 0;
    private int CUR_PAGE = 0;


    public MoviePresenter(Context ctx, final RecyclerView mRecyclerView, Bundle savedInstanceState) {
        context = ctx;
        recyclerView = mRecyclerView;
        mLayoutManager = new GridLayoutManager(context, 2);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d(TAG, "current page = " + CUR_PAGE + "/" + PAGE_SIZE);
                if (CUR_PAGE <= PAGE_SIZE) {
                    CUR_PAGE++;
                    loadNextDataFromApi(CUR_PAGE);
                }
            }


        };
        mRecyclerView.addOnScrollListener(scrollListener);
        Retrofit retrofit = retrofitBuilder();
        request = retrofit.create(RequestInterface.class);

        if(savedInstanceState == null) {
            Call<Page> item;
            if(mRecyclerView.getId() == R.id.recycler_view1) {
                item = request.getPopular(API_KEY, 1);
            } else {
                item = request.getTopRated(API_KEY, 1);
            }
            item.enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    allResult = response.body().getResults();
                    PAGE_SIZE = response.body().getTotalPages();
                    CUR_PAGE = response.body().getPage();
                    adapter = new MovieAdapter(context, allResult);
                    mRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            allResult = savedInstanceState.getParcelableArrayList(STATE_MOVIE+mRecyclerView.getId());
            PAGE_SIZE = savedInstanceState.getInt("pageSize"+recyclerView.getId());
            CUR_PAGE = savedInstanceState.getInt("curPage"+recyclerView.getId());
            adapter = new MovieAdapter(context, allResult);
            mRecyclerView.setAdapter(adapter);
        }

        Log.d(TAG, "View id = "+mRecyclerView.getId()
            +"\nMovies = "+CUR_PAGE+"/"+PAGE_SIZE);


    }



    private void loadNextDataFromApi(int page) {
        Call<Page> item = request.getPopular(API_KEY, page);
        item.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                List<Movies> movies = response.body().getResults();
                final int curSize = adapter.getItemCount();
                allResult.addAll(movies);

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, allResult.size() - 1);
                    }
                });

            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void setColumn(int column) {
        mLayoutManager.setSpanCount(column);
    }

    public void saveInstance(Bundle outState) {
        outState.putInt("pageSize"+recyclerView.getId(), PAGE_SIZE);
        outState.putInt("curPage"+recyclerView.getId(), CUR_PAGE);
        outState.putParcelableArrayList(STATE_MOVIE+recyclerView.getId(), (ArrayList<? extends Parcelable>) allResult);
    }




}