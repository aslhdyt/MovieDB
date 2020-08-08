package me.assel.moviedb.ui.genre

import android.app.usage.NetworkStats
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import me.assel.moviedb.R
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.ui.MainViewModel

class GenreTabFragment : Fragment(R.layout.fragment_genre_tab) {
    val vm: MainViewModel by viewModels({requireActivity()})


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {

            vm.genreList.observe(viewLifecycleOwner, Observer {
                /*todo loading*/
                if (it is NetworkState.Loading) {} else {}
                if (it is NetworkState.Success) {
                    val result = it.result
                    //TODO
                    println(result)
                } else handleErrorState(it)
            })
        }
    }
}