package com.moodup.movies.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.state.AddedItemState
import com.moodup.movies.state.UIState
import com.moodup.movies.ui.details.DetailsFragment.Companion.MOVIE_KEY
import com.moodup.movies.utils.adapter.MoviesAdapter
import com.moodup.movies.viewmodel.FavouritesViewModel
import com.moodup.movies.viewmodel.MovieViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var viewModel: MovieViewModel? = null
    private var favouritesViewModel : FavouritesViewModel? = null
    private var adapter: MoviesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(MovieViewModel::class.java)
            favouritesViewModel = ViewModelProvider(it).get(FavouritesViewModel::class.java)
        }

        setUpSearchView()
        setUpRecyclerView()

        observeLiveData()

    }

    private fun setUpRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        movies_recycler_view.layoutManager = linearLayoutManager

        movies_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount: Int = linearLayoutManager.itemCount
                val lastVisibleItem: Int = linearLayoutManager.findLastVisibleItemPosition()

                viewModel?.let {
                    if (!it.isDataLoading && lastVisibleItem == totalItemCount - 1) {
                        it.isDataLoading = true

                        if (it.checkIfThereIsScrollingPossible(totalItemCount)) {
                            adapter?.showFooterProgressBar()
                            it.getMovies(totalItemCount + 1, movie_searchview.query.toString())
                        }
                    }
                }
            }
        })
    }

    private fun setUpSearchView() {
        movie_searchview.clearFocus()

        movie_searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(query: String): Boolean {
                Log.d("STATE", "searchbar called on text change")
                viewModel?.UIstateLiveData?.postValue(UIState.LOADING)
                viewModel?.getMovies(0, query)
                adapter?.clearMoviesList()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("STATE", "searchbar called on submit text")
                viewModel?.UIstateLiveData?.postValue(UIState.LOADING)
                viewModel?.getMovies(0, query)
                adapter?.clearMoviesList()
                return false
            }

        })

    }

    private fun setUpAdapter(movies: List<Movie>) {
        adapter = MoviesAdapter()
        adapter?.updateAdapter(movies)
        movies_recycler_view.adapter = adapter
        movies_recycler_view.addItemDecoration(
            HorizontalDividerItemDecoration.Builder(context).color(
                Color.DKGRAY
            ).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build()
        )

        adapter!!.onItemClick = {
            val bundle = Bundle()
            bundle.putSerializable(MOVIE_KEY, it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }

        adapter!!.onFavouritesButtonClick = {
            favouritesViewModel?.addOrRemoveFromFavouritesList(it)
        }
    }

    private fun observeLiveData() {
        viewModel?.movieLiveData?.observe(viewLifecycleOwner, Observer {
            Log.d("STATE", "observer live data called ${it.size}")
            updateAdapter(it)
            viewModel?.isDataLoading = false
        })

        viewModel?.UIstateLiveData?.observe(viewLifecycleOwner, Observer { state ->
            Log.d("STATE", state.toString())
            when (state) {

                UIState.LOADING -> {
                    showProgressBar()
                }
                UIState.ON_ERROR -> {
                    showOnError()
                }
                UIState.ON_RESULT -> {
                    hideProgressBar()
                }
                UIState.ON_EMPTY_RESULTS -> {
                    showEmptyResults()
                }
                UIState.INITIALIZED -> {
                    viewModel?.UIstateLiveData?.postValue(UIState.LOADING)
                    viewModel?.getMovies(0, "")
                }
            }
        })

        favouritesViewModel?.favouritesLiveData?.observe(viewLifecycleOwner, Observer{
            adapter?.updateFavouritesList(it)
        })

        favouritesViewModel?.addedState?.observe(viewLifecycleOwner, Observer{addedState->
            when(addedState){
                AddedItemState.ON_ADDED ->{
                  Toast.makeText(context, R.string.added, Toast.LENGTH_SHORT).show()
                }
                AddedItemState.ON_REMOVED->{
                    Toast.makeText(context, R.string.removed, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun updateAdapter(movies: List<Movie>) {
        //setUpAdapter(movies)
        //todo: on details page and on back reset of adapter?

        if (adapter == null) {
            setUpAdapter(movies)
        } else {
            adapter!!.updateAdapter(movies)
        }
    }

    private fun showEmptyResults() {
        movies_recycler_view.visibility = View.GONE
        results_textView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun showOnError() {
        movies_recycler_view.visibility = View.GONE
        results_textView.visibility = View.VISIBLE
        results_textView.text = R.string.error.toString()
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        movies_recycler_view.visibility = View.GONE
        results_textView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        movies_recycler_view.visibility = View.VISIBLE
        results_textView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }


}