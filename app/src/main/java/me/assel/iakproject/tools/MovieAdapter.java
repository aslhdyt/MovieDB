package me.assel.iakproject.tools;

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

/**
 * Created by assel on 5/23/17.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private List<Movies.Result> moviesList;

    MovieAdapter(Context mContext, List<Movies.Result> moviesList) {
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
        Log.d("RecyclerView", "item show= "+position);
        final Movies.Result result = moviesList.get(position);

        // loading album cover using Glide library
        Picasso.with(mContext).load(MoviePresenter.IMG_BASE_URL+ result.getPoster_path()).into(holder.poster);


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
