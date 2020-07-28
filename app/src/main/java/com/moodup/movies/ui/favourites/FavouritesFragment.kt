package com.moodup.movies.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.state.FavouritesCallbackState
import com.moodup.movies.ui.favourites.adapter.FavouritesAdapter
import com.moodup.movies.viewmodel.favourites.FavouritesViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {
    private var adapter: FavouritesAdapter? = null
    private lateinit var viewModel: FavouritesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(FavouritesViewModel::class.java)
        }

        favourites_grid_recycler_view.layoutManager = GridLayoutManager(context, 2)
        adapter = FavouritesAdapter()
        favourites_grid_recycler_view.adapter = adapter

        adapter!!.onItemClick = { movie ->
            showDeleteDialog(movie)
        }

        viewModel.getFavouriteMoviesFromDatabase()
        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.favouritesMovies.observe(viewLifecycleOwner, Observer {
            adapter?.setData(it)
        })

        viewModel.favouritesCallbackState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                FavouritesCallbackState.EMPTY_LIST -> {
                    showOnEmptyList()
                }

                FavouritesCallbackState.ON_FAILURE -> {
                    showOnFailure()
                }

                FavouritesCallbackState.ON_SUCCESS -> {
                    onSuccess()
                }
                FavouritesCallbackState.INITIALIZED -> {
                    viewModel.favouritesCallbackState.postValue(FavouritesCallbackState.LOADING)
                    viewModel.getFavouriteMoviesFromDatabase()
                }
                FavouritesCallbackState.LOADING -> {
                    showLoading()
                }
            }
        })
    }

    private fun showDeleteDialog(movie: Movie) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(context?.resources?.getString(R.string.dialog_title))

        builder?.setPositiveButton(context?.resources?.getString(R.string.remove_yes)) { dialog, which ->
            viewModel.removeFromFavourite(movie)
        }

        builder?.setNegativeButton(android.R.string.cancel) { dialog, which ->
            showCancelledMessage()
        }
        builder?.show()
    }

    private fun showCancelledMessage() {
        Toast.makeText(
            context,
            context?.resources?.getString(R.string.cancelled), Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoading() {
        favourites_progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        favourites_progressBar.visibility = View.GONE
    }

    private fun showOnEmptyList() {
        favourites_grid_recycler_view.visibility = View.GONE
        empty_favourite_list.visibility = View.VISIBLE
        error_img.visibility = View.GONE
    }

    private fun showOnFailure() {
        favourites_grid_recycler_view.visibility = View.GONE
        empty_favourite_list.visibility = View.GONE
        error_img.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        favourites_grid_recycler_view.visibility = View.VISIBLE
        empty_favourite_list.visibility = View.GONE
        error_img.visibility = View.GONE
        hideLoading()
    }
}