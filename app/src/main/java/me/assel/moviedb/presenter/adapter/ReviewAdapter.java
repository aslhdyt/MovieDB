package me.assel.moviedb.presenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.assel.moviedb.R;
import me.assel.moviedb.model.Reviews;

/**
 * Created by assel on 5/26/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final List<Reviews.Result> reviewList;
    private final Context context;

    public ReviewAdapter(Context context, List<Reviews.Result> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);
        return new ViewHolder(itemsView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reviews.Result items = reviewList.get(position);
        holder.author.setText(items.getAuthor());
        holder.content.setText(items.getContent());

    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        ViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.textView_title);
            content = (TextView) view.findViewById(R.id.textView_content);
        }
    }
}