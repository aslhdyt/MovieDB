package me.assel.iakproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.assel.iakproject.DetailsActivity;
import me.assel.iakproject.R;
import me.assel.iakproject.api.response.Movies;

import static me.assel.iakproject.api.ConnectionData.IMG_BASE_URL;

/**
 * Created by assel on 5/23/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private List<Movies.Result> moviesList;

    public MovieAdapter(Context mContext, List<Movies.Result> moviesList) {
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movies.Result result = moviesList.get(position);

        // loading album cover using Retrofit library
        Picasso.with(mContext).load(IMG_BASE_URL+ result.getPoster_path()).placeholder(R.drawable.movie).into(holder.poster);


        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "Result title = "+ result.getTitle());
                Intent i = new Intent(mContext, DetailsActivity.class);
                i.putExtra("result", result);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;

        ViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.poster);
        }
    }
}
