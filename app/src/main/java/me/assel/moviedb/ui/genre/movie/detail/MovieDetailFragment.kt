package me.assel.moviedb.ui.genre.movie.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.assel.moviedb.AppConfig
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieDetailBinding
import me.assel.moviedb.databinding.ViewHolderVideoBinding
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.utils.*
import me.assel.moviedb.datasource.network.model.response.MovieVideoResponse.Result as Video

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
            val videoAdapter = object : RecyclerView.Adapter<VideoViewHolder>() {
                var list: List<Video> = emptyList()
                set(value) {
                    field = value
                    notifyDataSetChanged()
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder = VideoViewHolder(parent)
                override fun getItemCount(): Int = list.size
                override fun onBindViewHolder(holder: VideoViewHolder, position: Int) = holder.onBind(list[position])
            }
            rvVideos.adapter = videoAdapter


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

//
//            //getTrailerVideos
//
//
//            //getTrailerVideos
//            call.enqueue(object : Callback<Videos> {
//                override fun onResponse(call: Call<Videos>, response: Response<Videos>) {
//                    val result = response.body()!!.results
//                    videos = result
//                    val adapter = VideoAdapter(getBaseContext(), result)
//                    val recyclerView = findViewById<View>(R.id.recyclerView_trailer) as RecyclerView
//                    val manager = LinearLayoutManager(getBaseContext())
//                    manager.isAutoMeasureEnabled = true
//                    recyclerView.layoutManager = manager
//                    recyclerView.adapter = adapter
//                }
//
//                override fun onFailure(call: Call<Videos>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })
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

    class VideoViewHolder(v: ViewGroup): RecyclerView.ViewHolder(v.inflate(R.layout.view_holder_video)) {
        fun onBind(data: Video) = with(ViewHolderVideoBinding.bind(itemView)) {
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