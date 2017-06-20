package me.assel.moviedb.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.realm.RealmResults;
import me.assel.moviedb.DbDetailActivity;
import me.assel.moviedb.R;
import me.assel.moviedb.contentProvider.DbObject;

import static me.assel.moviedb.AppConfig.IMG_BASE_URL;

/**
 * Created by assel on 5/28/17.
 */

public class DbAdapter extends RecyclerView.Adapter<DbAdapter.ViewHolder> {
    private Activity mContext;
    private RealmResults<DbObject> moviesList;
    public DbAdapter(Activity mContext, RealmResults<DbObject> list) {
        this.mContext = mContext;
        this.moviesList = list;
    }

    @Override
    public DbAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DbAdapter.ViewHolder holder, int position) {
        final DbObject result = moviesList.get(position);

        Picasso.with(mContext).load(IMG_BASE_URL+ result.getImgUrl()).placeholder(R.drawable.video).into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "Result title = "+ result.getTitle());
                Intent i = new Intent(mContext, DbDetailActivity.class);
                i.putExtra("id", result.getId());
                mContext.startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        public ViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
