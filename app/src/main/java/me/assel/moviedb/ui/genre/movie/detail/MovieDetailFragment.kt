package me.assel.moviedb.ui.genre.movie.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import me.assel.moviedb.R

class MovieDetailFragment: Fragment(R.layout.fragment_movie_detail) {

    companion object {
        private const val ARG_MOVIE_ID = "arg-movie_id"
        fun arg(movieId: Int) = bundleOf(ARG_MOVIE_ID to movieId)
    }
}