package me.assel.moviedb;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TabHost;

import io.realm.Realm;
import me.assel.moviedb.presenter.FromDBPresenter;
import me.assel.moviedb.presenter.FromApiPresenter;

public class MainActivity extends Activity {
    private FromApiPresenter moviePresenter1, moviePresenter2;
    private FromDBPresenter moviePresenter3;
    TabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);

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

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                    Log.d("tab num", String.valueOf(host.getCurrentTab()));
            }
        });


        if(savedInstanceState != null) {
            host.setCurrentTab(savedInstanceState.getInt("Tab"));
        }

        RecyclerView mRecycler1 = (RecyclerView) findViewById(R.id.recycler_view1);
        moviePresenter1 = new FromApiPresenter(this, mRecycler1, savedInstanceState);

        RecyclerView mRecycler2 = (RecyclerView) findViewById(R.id.recycler_view2);
        moviePresenter2 = new FromApiPresenter(this, mRecycler2, savedInstanceState);

        RecyclerView mRecycler3 = (RecyclerView) findViewById(R.id.recycler_view3);
        moviePresenter3 = new FromDBPresenter(this, mRecycler3, savedInstanceState);

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

//    private void refreshRealm() {
//        moviePresenter3.refresh();
//    }

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
        outState.putInt("Tab",host.getCurrentTab());
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("Activity", "return from requestCode = "+requestCode
//            +"\nresultCode = "+resultCode);
//        if(requestCode == 1) {
//            refreshRealm();
//        }
//
//    }

}
