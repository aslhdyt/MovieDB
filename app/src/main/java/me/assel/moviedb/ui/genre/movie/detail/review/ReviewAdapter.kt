package me.assel.moviedb.ui.genre.movie.detail.review

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.assel.moviedb.R
import me.assel.moviedb.databinding.ViewHolderReviewBinding
import me.assel.moviedb.datasource.network.model.response.MovieReviewResponse
import me.assel.moviedb.utils.inflate
import me.assel.moviedb.datasource.network.model.response.MovieReviewResponse.Result as Review

class ReviewAdapter : PagedListAdapter<Review, ReviewAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: MovieReviewResponse.Result, newItem: MovieReviewResponse.Result): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieReviewResponse.Result, newItem: MovieReviewResponse.Result): Boolean =
            oldItem == newItem

}) {
    class ViewHolder(v: ViewGroup): RecyclerView.ViewHolder(v.inflate(R.layout.view_holder_review)) {
        fun onBind(data: Review?)= with(ViewHolderReviewBinding.bind(itemView)) {
            if (data == null) {
                tvAuthor.text = null
                tvContent.text = null
            } else {
                tvAuthor.text = data.author
                tvContent.text = data.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.onBind(data)
    }
}
