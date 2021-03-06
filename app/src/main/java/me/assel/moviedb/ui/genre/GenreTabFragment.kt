package me.assel.moviedb.ui.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import me.assel.moviedb.R
import me.assel.moviedb.databinding.FragmentGenreTabBinding
import me.assel.moviedb.datasource.model.NetworkState
import me.assel.moviedb.datasource.model.handleErrorState
import me.assel.moviedb.datasource.network.model.response.GenreListResponse
import me.assel.moviedb.ui.genre.movie.MovieListFragment

class GenreTabFragment : Fragment(R.layout.fragment_genre_tab) {
    private val vm: GenreViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState) ?: return null
        FragmentGenreTabBinding.bind(v).apply {
            val adapter = PageAdapter(this@GenreTabFragment)
                viewPager.adapter = adapter
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    val title = adapter.getTitle(position)
                    tab.text = title
                }.attach()

            vm.genreList.observe(viewLifecycleOwner, Observer {
                if (it is NetworkState.Loading) progressBar.show() else progressBar.hide()
                if (it is NetworkState.Success) {
                    val result = it.result
                    adapter.list = result.genres

                    val lastPos = vm.pagePosition
                    if (lastPos != null) {
                        viewPager.setCurrentItem(lastPos, false)
                    }
                } else handleErrorState(it)
            })

            //saving state
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    vm.pagePosition = position
                }
            })

        }
        return v
    }



    inner class PageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        var list = emptyList<GenreListResponse.Genre>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        override fun getItemCount(): Int = list.size

        override fun createFragment(position: Int): Fragment {
            val id = list[position].id
            return MovieListFragment.newInstance(id)
        }

        fun getTitle(position: Int) = list[position].name
    }
}