package me.assel.moviedb.ui.genre.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentMovieListBinding

class MovieListFragment private constructor(): Fragment(R.layout.fragment_movie_list) {


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
            FragmentMovieListBinding.bind(this).run {
                textView.text = "id = $id"
            }
        }
    }
}