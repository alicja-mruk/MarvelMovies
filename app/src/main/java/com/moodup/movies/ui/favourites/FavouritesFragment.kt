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
import com.example.movies.databinding.FragmentFavouritesBinding
import com.example.movies.databinding.FragmentHomeBinding
import com.moodup.movies.model.Movie
import com.moodup.movies.state.FavouritesCallbackState
import com.moodup.movies.ui.favourites.adapter.FavouritesAdapter
import com.moodup.movies.viewmodel.favourites.FavouritesViewModel
import kotlinx.android.synthetic.main.fragment_favourites.*


class FavouritesFragment : Fragment() {
    private lateinit var binding: FragmentFavouritesBinding
    private var adapter: FavouritesAdapter? = null
    private lateinit var viewModel: FavouritesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(FavouritesViewModel::class.java)
        }

        binding.favouritesGridRecyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = FavouritesAdapter()
        binding.favouritesGridRecyclerView.adapter = adapter

        adapter?.let {
            it.onItemClick = { movie ->
                showDeleteDialog(movie)
            }
        }

        viewModel.getFavouriteMoviesFromDatabase()
        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.favouritesMovies.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){
                adapter?.setData(it)
            }
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
        binding.favouritesProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.favouritesProgressBar.visibility = View.GONE
    }

    private fun showOnEmptyList() {
        binding.favouritesGridRecyclerView.visibility = View.GONE
        binding.emptyFavouriteList.visibility = View.VISIBLE
        binding.errorImg.visibility = View.GONE
        hideLoading()
    }

    private fun showOnFailure() {
        binding.favouritesGridRecyclerView.visibility = View.GONE
        binding.emptyFavouriteList.visibility = View.GONE
        binding.errorImg.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        binding.favouritesGridRecyclerView.visibility = View.VISIBLE
        binding.emptyFavouriteList.visibility = View.GONE
        binding.errorImg.visibility = View.GONE
        hideLoading()
    }
}