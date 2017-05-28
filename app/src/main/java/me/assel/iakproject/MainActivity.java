package me.assel.iakproject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TabHost;

import io.realm.Realm;
import io.realm.RealmResults;
import me.assel.iakproject.adapter.DBPresenter;
import me.assel.iakproject.adapter.MoviePresenter;
import me.assel.iakproject.db.DbObject;

public class MainActivity extends Activity {
    String TAG = "MainActivity";
    private MoviePresenter moviePresenter1, moviePresenter2;
    private DBPresenter moviePresenter3;
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
                if (tabId.equals("Favourite")) {
                    refreshRealm();
                }
            }
        });


        if(savedInstanceState != null) {
            host.setCurrentTab(savedInstanceState.getInt("Tab"));
        }

        RecyclerView mRecycler1 = (RecyclerView) findViewById(R.id.recycler_view1);
        moviePresenter1 = new MoviePresenter(this, mRecycler1, savedInstanceState);

        RecyclerView mRecycler2 = (RecyclerView) findViewById(R.id.recycler_view2);
        moviePresenter2 = new MoviePresenter(this, mRecycler2, savedInstanceState);

        RecyclerView mRecycler3 = (RecyclerView) findViewById(R.id.recycler_view3);
        moviePresenter3 = new DBPresenter(this, mRecycler3, savedInstanceState);


        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            moviePresenter1.setColumn(2);
            moviePresenter2.setColumn(2);
            moviePresenter3.setColumn(4);
        }
        else{
            moviePresenter1.setColumn(4);
            moviePresenter2.setColumn(4);
            moviePresenter3.setColumn(4);
        }
    }

    private void refreshRealm() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<DbObject> result = realm.where(DbObject.class).findAll();
        Log.d("DB", String.valueOf(result.size()));
        for(DbObject obj : result) {
            Log.d("DB", obj.toString());
        }
        realm.commitTransaction();
        moviePresenter3.refresh();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        moviePresenter1.saveInstance(outState);
        moviePresenter2.saveInstance(outState);
        outState.putInt("Tab",host.getCurrentTab());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            refreshRealm();
        }

    }
}