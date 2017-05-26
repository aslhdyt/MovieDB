package me.assel.iakproject;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TabHost;

import me.assel.iakproject.tools.MoviePresenter;

public class MainActivity extends Activity {
    String TAG = "MainActivity";
    private MoviePresenter moviePresenter1, moviePresenter2;
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
        spec.setContent(R.id.textView4);
        spec.setIndicator("Favourite");
        host.addTab(spec);

        if(savedInstanceState != null) {
            host.setCurrentTab(savedInstanceState.getInt("Tab"));
        }

        RecyclerView mRecycler1 = (RecyclerView) findViewById(R.id.recycler_view1);
        moviePresenter1 = new MoviePresenter(this, mRecycler1, savedInstanceState);

        RecyclerView mRecycler2 = (RecyclerView) findViewById(R.id.recycler_view2);
        moviePresenter2 = new MoviePresenter(this, mRecycler2, savedInstanceState);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            moviePresenter1.setColumn(2);
            moviePresenter2.setColumn(2);
        }
        else{
            moviePresenter1.setColumn(4);
            moviePresenter2.setColumn(4);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        moviePresenter1.saveInstance(outState);
        moviePresenter2.saveInstance(outState);
        outState.putInt("Tab",host.getCurrentTab());
    }
}
