package me.assel.moviedb.presenter.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.assel.moviedb.DetailsActivity;
import me.assel.moviedb.R;
import me.assel.moviedb.api.response.Movies;

import static me.assel.moviedb.AppConfig.IMG_BASE_URL;

/**
 * Created by assel on 5/28/17.
 */

public class DbAdapter extends RecyclerView.Adapter<DbAdapter.ViewHolder> {
    private Activity mContext;
    private List<Movies> movieList;
    public DbAdapter(Activity mContext, List<Movies> data) {
        this.mContext = mContext;
        this.movieList = data;
    }

    @Override
    public DbAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DbAdapter.ViewHolder holder, int position) {
//        final DbObject result = movieList.get(position);
        final Movies result = movieList.get(position);

        Picasso.with(mContext).load(IMG_BASE_URL+ result.getPosterPath()).placeholder(R.drawable.video).into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "Result title = "+ result.getTitle());
                Intent i = new Intent(mContext, DetailsActivity.class);
                i.putExtra("id", result.getId());
                mContext.startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        public ViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
}
