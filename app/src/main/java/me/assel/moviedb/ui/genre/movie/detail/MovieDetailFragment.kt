package me.assel.moviedb.ui.genre.movie.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import me.assel.moviedb.AppConfig
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieDetailBinding
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.ui.genre.movie.detail.review.ReviewAdapter
import me.assel.moviedb.ui.genre.movie.detail.video.VideoAdapter
import me.assel.moviedb.utils.*

class MovieDetailFragment: Fragment(R.layout.fragment_movie_detail) {
    companion object {

        private const val ARG_MOVIE_ID = "arg-movie_id"
        fun arg(movieId: Int) = bundleOf(ARG_MOVIE_ID to movieId)
    }
    private val vm: MovieDetailViewModel by viewModels { viewModelFactory {
        val movieId = requireArguments().getInt(ARG_MOVIE_ID)
        MovieDetailViewModel(movieId, requireActivity().application)
    } }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState) ?: return null
        FragmentMovieDetailBinding.bind(v).apply {

            //init tab
            tabHost.setup()
            tabHost.addTab(tabHost.newTabSpec("Videos").apply {
                setContent(R.id.lytVideos)
                setIndicator("Videos")
            })
            tabHost.addTab(tabHost.newTabSpec("Reviews").apply {
                setContent(R.id.lytReviews)
                setIndicator("Reviews")
            })

            //init view
            rvVideos.layoutManager = LinearLayoutManager(context)
            val videoAdapter = VideoAdapter()
            rvVideos.adapter = videoAdapter

            //review
            rvReviews.layoutManager = LinearLayoutManager(context)
            val reviewAdapter = ReviewAdapter()
            rvReviews.adapter = reviewAdapter

            vm.detail.observe(viewLifecycleOwner, Observer {
                tabHost.gone()
                if (it is NetworkState.Loading) { progressBar.show() } else { progressBar.hide() }
                if (it is NetworkState.Success) {
                    val movie = it.result
                    imageViewPoster.loadImage(AppConfig.IMG_BASE_URL+movie.posterPath)
                    collapsingToolbar.title = movie.title
                    collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
                    tvRelease.text = movie.releaseDate
                    tvOverview.text = movie.overview
                    imageViewStar.visible()
                    textViewStar.text = movie.voteAverage.toString()

                    tabHost.visible()
                } else handleErrorState(it)
            })

            vm.videos.observe(viewLifecycleOwner, Observer {
                if (it is NetworkState.Loading) pbVideos.show() else pbVideos.hide()
                if (it is NetworkState.Success) {
                    val videos = it.result.results
                    videoAdapter.list = videos
                } else {
                    handleErrorState(it)
                }
            })
            vm.reviews.observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                reviewAdapter.submitList(it)
            })
            vm.reviewNetworkState.observe(viewLifecycleOwner, Observer {
                if (it is NetworkState.Loading) pbReviews.show() else pbReviews.hide()
                if (it is NetworkState.Success) {
                    //handled in pagedList
                } else handleErrorState(it)
            })

//
//            val call1: Call<Reviews> = request.getReviews(movie.getId(), AppConfig.API_KEY)
//            call1.enqueue(object : Callback<Reviews> {
//                override fun onResponse(call: Call<Reviews>, response: Response<Reviews>) {
//                    val result = response.body()!!.results
//                    reviews = result
//                    Log.d("review", response.toString())
//                    val adapter = ReviewAdapter(getBaseContext(), result)
//                    val recyclerView = findViewById<View>(R.id.recyclerView_review) as RecyclerView
//                    val manager = LinearLayoutManager(getBaseContext())
//                    manager.isAutoMeasureEnabled = true
//                    recyclerView.layoutManager = manager
//                    recyclerView.adapter = adapter
//                }
//
//                override fun onFailure(call: Call<Reviews>, t: Throwable) {}
//            })
//
//            val like = findViewById<View>(R.id.imageView_like) as ImageView
//            like.tag = R.drawable.unlike

        return v
        }

    }

}
