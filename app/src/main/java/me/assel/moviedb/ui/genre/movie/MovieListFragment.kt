package me.assel.moviedb.ui.genre.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import me.assel.moviedb.AppConfig.IMG_BASE_URL
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieListBinding
import me.assel.moviedb.databinding.ViewHolderMovieBinding
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.datasource.network.model.response.DiscoverMovieResponse
import me.assel.moviedb.ui.MainViewModel
import me.assel.moviedb.utils.inflate
import me.assel.moviedb.utils.loadImage
import me.assel.moviedb.utils.showToast

class MovieListFragment private constructor(): Fragment(R.layout.fragment_movie_list) {
    val vm: MainViewModel by viewModels({requireActivity()})

    companion object {
        const val ARG_GENRE_ID = "arg-genre_id"
        fun newInstance(id: Int): MovieListFragment {
            return MovieListFragment().apply {
                arguments = bundleOf(ARG_GENRE_ID to id)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            val id = requireArguments().getInt(ARG_GENRE_ID)
            val adapter = MovieListAdapter {
                showToast("TODO: ${it.originalTitle}")
            }
            val bind = FragmentMovieListBinding.bind(this)

            bind.recyclerView.adapter = adapter

            vm.getMoviesByGenreIds(id).observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                if (it is NetworkState.Loading) {} else {} //TODO loading
                if (it is NetworkState.Success) {
                    adapter.list = it.result.results
                } else handleErrorState(it)
            })
        }
    }


    internal class MovieListAdapter(val onClick: (DiscoverMovieResponse.Result)->Unit): RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
        var list: List<DiscoverMovieResponse.Result> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        internal inner class ViewHolder(v: ViewGroup): RecyclerView.ViewHolder(
                v.inflate(R.layout.view_holder_movie)) {
            fun bind(data: DiscoverMovieResponse.Result) = with(ViewHolderMovieBinding.bind(itemView)) {
                root.setOnClickListener { onClick(data) }
                imageView.loadImage(IMG_BASE_URL+data.posterPath)
                textView2.text = data.originalTitle
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = list[position]
            holder.bind(data)
        }

    }

}
