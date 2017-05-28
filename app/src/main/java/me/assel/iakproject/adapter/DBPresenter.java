package me.assel.iakproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import me.assel.iakproject.MainActivity;
import me.assel.iakproject.db.DbObject;

/**
 * Created by assel on 5/28/17.
 */

public class DBPresenter {
    private Context context;
    private RecyclerView recyclerView;
    private GridLayoutManager mLayoutManager;

    private DbAdapter adapter;

    public DBPresenter(MainActivity ctx, RecyclerView mRecyclerView, Bundle savedInstanceState) {
        context = ctx;
        recyclerView = mRecyclerView;
        mLayoutManager = new GridLayoutManager(context, 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<DbObject> list = realm.where(DbObject.class)
                .findAll();
        realm.commitTransaction();

        adapter = new DbAdapter((Activity) context, list);
        mRecyclerView.setAdapter(adapter);

    }
    public void setColumn(int column) {
        mLayoutManager.setSpanCount(column);
    }

    public void refresh() {
        recyclerView.setAdapter(adapter);
    }
}
