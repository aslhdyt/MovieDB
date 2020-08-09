package me.assel.moviedb.ui.genre.movie

import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.assel.moviedb.AppConfig.IMG_BASE_URL
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieListBinding
import me.assel.moviedb.databinding.ViewHolderMovieBinding
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.datasource.network.model.response.DiscoverMovieResponse
import me.assel.moviedb.ui.genre.movie.detail.MovieDetailFragment
import me.assel.moviedb.utils.*

class MovieListFragment: Fragment(R.layout.fragment_movie_list) {
    private val vm: ViewModel by viewModels {
        val genreId = requireArguments().getInt(ARG_GENRE_ID)
        viewModelFactory { ViewModel(requireActivity().application, genreId) }
    }

    companion object {
        const val ARG_GENRE_ID = "arg-genre_id"
        fun newInstance(id: Int): MovieListFragment {
            return MovieListFragment().apply {
                arguments = bundleOf(ARG_GENRE_ID to id)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState) ?: return null
        FragmentMovieListBinding.bind(v).apply {
            val adapter = MovieListAdapter {
                val arg = MovieDetailFragment.arg(it.id)
                findNavController().navigate(R.id.action_genreFragment_to_movieDetailFragment, arg)
            }
            val layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

            vm.dataSource.observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                adapter.submitList(it)
            })
            vm.initState.observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                if (it is NetworkState.Loading) initProgress.show() else initProgress.hide()
                //result handled in paging
                handleErrorState(it)
            })
            vm.loadMoreState.observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                if (it is NetworkState.Loading) loadMoreProgress.show() else loadMoreProgress.hide()
                //result handled in paging
                handleErrorState(it)
            })

        }
        return v
    }

    internal class ViewModel(application: Application, genreId: Int): AndroidViewModel(application) {
        private val sourceFactory = DiscoverMovieDataSource.Factory(genreId)
        val dataSource = LivePagedListBuilder(
                sourceFactory,
                PagedList.Config.Builder()
                        .setPrefetchDistance(10)
                        .build()
        ).build()

        val initState = sourceFactory.dataSource.switchMap {
            it.initNetworkState
        }

        val loadMoreState = sourceFactory.dataSource.switchMap {
            it.loadMoreNetworkState
        }
    }

    internal class MovieListAdapter(val onClick: (DiscoverMovieResponse.Result)->Unit): PagedListAdapter<DiscoverMovieResponse.Result, MovieListAdapter.ViewHolder>(
            object : DiffUtil.ItemCallback<DiscoverMovieResponse.Result>() {
                // The ID property identifies when items are the same.
                override fun areItemsTheSame(oldItem: DiscoverMovieResponse.Result, newItem: DiscoverMovieResponse.Result) = oldItem.id == newItem.id
                // If you use the "==" operator, make sure that the object implements
                // .equals(). Alternatively, write custom data comparison logic here.
                override fun areContentsTheSame(oldItem: DiscoverMovieResponse.Result, newItem: DiscoverMovieResponse.Result) = oldItem == newItem
    }) {
        internal inner class ViewHolder(v: ViewGroup): RecyclerView.ViewHolder(
                v.inflate(R.layout.view_holder_movie)) {
            fun bind(data: DiscoverMovieResponse.Result?) = with(ViewHolderMovieBinding.bind(itemView)) {
                if (data != null) {
                    root.setOnClickListener { onClick(data) }
                    imageView.loadImage(IMG_BASE_URL+data.posterPath)
                    textView2.text = data.originalTitle
                } else { // placeholdeer
                    root.setOnClickListener(null)
                    imageView.setImageResource(0)
                    textView2.text = root.context.getString(R.string.label_loading)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val data = getItem(position)
            holder.bind(data)
        }
    }

}
