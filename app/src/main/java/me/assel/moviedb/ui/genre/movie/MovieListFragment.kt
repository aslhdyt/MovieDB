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
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieListBinding
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.datasource.network.model.response.DiscoverMovieResponse
import me.assel.moviedb.ui.MainViewModel

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

            }
            val bind = FragmentMovieListBinding.bind(this)

            bind.recyclerView.adapter = adapter

            vm.getMoviesByGenreIds(id).observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                println("data: $it")
                handleErrorState(it)
            })
        }
    }


    internal class MovieListAdapter(val onClick: (DiscoverMovieResponse.Result)->Unit): RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
        var list: List<DiscoverMovieResponse.Result> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        internal inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
            fun bind(data: DiscoverMovieResponse.Result) = with(itemView){
                setOnClickListener { onClick(data) }
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
