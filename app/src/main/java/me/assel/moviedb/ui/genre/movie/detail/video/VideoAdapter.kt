package me.assel.moviedb.ui.genre.movie.detail.video

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.assel.moviedb.R
import me.assel.moviedb.databinding.ViewHolderVideoBinding
import me.assel.moviedb.datasource.network.model.response.MovieVideoResponse
import me.assel.moviedb.utils.inflate

class VideoAdapter: RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
        var list: List<MovieVideoResponse.Result> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)
        override fun getItemCount(): Int = list.size
        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind(list[position])

    class ViewHolder(v: ViewGroup): RecyclerView.ViewHolder(v.inflate(R.layout.view_holder_video)) {
        fun onBind(data: MovieVideoResponse.Result) = with(ViewHolderVideoBinding.bind(itemView)) {
            tvTitle.text = data.name
            tvType.text = data.type

            if (data.site != "YouTube") {
                ivYoutube.setImageResource(0)
            } else {
                ivYoutube.setImageResource(R.drawable.youtube)
            }

            root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + data.key))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                it.context.startActivity(intent)
            }
        }
    }
    }