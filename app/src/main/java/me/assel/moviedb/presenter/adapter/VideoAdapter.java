package me.assel.moviedb.presenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import me.assel.moviedb.R;
import me.assel.moviedb.model.Videos;

/**
 * Created by assel on 5/26/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final List<Videos.Result> videoList;
    private final Context context;

    public VideoAdapter(Context context, List<Videos.Result> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list, parent, false);
        return new ViewHolder(itemsView);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
        final Videos.Result items = videoList.get(position);

        if(!videoList.get(position).getSite().equals("YouTube")) {
            holder.icon.setImageResource(0);
        }

        holder.title.setText(items.getName());
        holder.type.setText(items.getType());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + items.getKey()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, type;
        RelativeLayout parent;
        ImageView icon;

        ViewHolder(View view) {
            super(view);
            icon = (ImageView)view.findViewById(R.id.imageView_icon);
            parent = (RelativeLayout)view.findViewById(R.id.relative_layout_listVideo);
            title = (TextView) view.findViewById(R.id.textView_author);
            type = (TextView) view.findViewById(R.id.textView_type);
        }
    }
}