package com.moodup.movies.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.moodup.movies.utils.adapter.FavouritesMoviesAdapter
import com.moodup.movies.viewmodel.FavouritesViewModel
import com.moodup.movies.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : Fragment() {
    private var favouritesViewModel : FavouritesViewModel? = null
    private var adapter: FavouritesMoviesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavouritesMoviesAdapter()
        favourites_grid_recycler_view.layoutManager =  GridLayoutManager(context,2)
        favourites_grid_recycler_view.adapter = adapter

        activity?.let {
            favouritesViewModel = ViewModelProvider(it).get(FavouritesViewModel::class.java)
        }
        observeLiveData()

    }
    private fun observeLiveData(){
        favouritesViewModel?.favouritesLiveData?.observe(viewLifecycleOwner, Observer {
            adapter?.setData(it)
        })

    }
}