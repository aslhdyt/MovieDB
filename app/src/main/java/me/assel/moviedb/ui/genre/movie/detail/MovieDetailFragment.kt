package me.assel.moviedb.ui.genre.movie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieDetailBinding

class MovieDetailFragment: Fragment(R.layout.fragment_movie_detail) {

    companion object {
        private const val ARG_MOVIE_ID = "arg-movie_id"
        fun arg(movieId: Int) = bundleOf(ARG_MOVIE_ID to movieId)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState) ?: return null
        FragmentMovieDetailBinding.bind(v).apply {

        }
        return v
    }
}