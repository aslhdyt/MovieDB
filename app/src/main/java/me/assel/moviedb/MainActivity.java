package me.assel.moviedb;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TabHost;

import me.assel.moviedb.presenter.FromApiPresenter;
import me.assel.moviedb.presenter.FromDBPresenter;

public class MainActivity extends Activity {
    private FromApiPresenter moviePresenter1, moviePresenter2;
    private FromDBPresenter moviePresenter3;
    private SwipeRefreshLayout refreshLayout;
    TabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init tab
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //tab1
        TabHost.TabSpec spec = host.newTabSpec("Popular");
        spec.setContent(R.id.recycler_view1);
        spec.setIndicator("Popular");
        host.addTab(spec);

        //tab2
        spec = host.newTabSpec("Top Rated");
        spec.setContent(R.id.recycler_view2);
        spec.setIndicator("Top Rated");
        host.addTab(spec);

        //tab3
        spec = host.newTabSpec("Favourite");
        spec.setContent(R.id.recycler_view3);
        spec.setIndicator("Favourite");
        host.addTab(spec);




        if(savedInstanceState != null) {
            host.setCurrentTab(savedInstanceState.getInt("Tab"));
        }

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //check on which tab;
                switch (host.getCurrentTab()) {
                    case 0:
                        moviePresenter1.loadData();
                        break;
                    case 1:
                        moviePresenter2.loadData();
                        break;
                    case 2:
                        moviePresenter3.loadData();
                        break;
                }
            }
        });

        RecyclerView mRecycler1 = (RecyclerView) findViewById(R.id.recycler_view1);
        moviePresenter1 = new FromApiPresenter(this, mRecycler1, savedInstanceState, refreshLayout);

        RecyclerView mRecycler2 = (RecyclerView) findViewById(R.id.recycler_view2);
        moviePresenter2 = new FromApiPresenter(this, mRecycler2, savedInstanceState, refreshLayout);

        RecyclerView mRecycler3 = (RecyclerView) findViewById(R.id.recycler_view3);
        moviePresenter3 = new FromDBPresenter(this, mRecycler3, savedInstanceState, refreshLayout);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            moviePresenter1.setColumn(2);
            moviePresenter2.setColumn(2);
            moviePresenter3.setColumn(2);
        }
        else{
            moviePresenter1.setColumn(4);
            moviePresenter2.setColumn(4);
            moviePresenter3.setColumn(4);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        moviePresenter3.onResume(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        moviePresenter1.saveInstance(outState);
        moviePresenter2.saveInstance(outState);
        moviePresenter3.saveInstance(outState);
        outState.putInt("Tab",host.getCurrentTab());
    }


}
