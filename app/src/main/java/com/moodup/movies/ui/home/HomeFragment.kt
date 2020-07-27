package com.moodup.movies.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.jakewharton.rxbinding2.widget.queryTextChanges
import com.moodup.movies.state.UIState
import com.moodup.movies.ui.details.DetailsFragment.Companion.MOVIE_KEY
import com.moodup.movies.utils.adapter.MoviesAdapter
import com.moodup.movies.viewmodel.MovieViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var viewModel: MovieViewModel? = null
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
        }

        setUpSearchView()
        setUpRecyclerView()
        setUpAdapter()
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

    @SuppressLint("CheckResult")
    private fun setUpSearchView() {
        movie_searchview.clearFocus()

        movie_searchview.queryTextChanges().skip(2)
            .map { it.toString() }
            .doOnNext {
                viewModel?.UIstateLiveData?.postValue(UIState.LOADING)
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .subscribe {
                if (movie_searchview.hasFocus()) {
                    viewModel?.onSearchQueryChanged(it)
                }
            }

    }

    private fun setUpAdapter() {

        viewModel?.let { viewModel ->
            adapter = MoviesAdapter(viewModel, this)
            movies_recycler_view.adapter = adapter
            movies_recycler_view.addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context).color(
                    Color.DKGRAY
                ).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                    .build()
            )
        }

        adapter!!.onItemClick = {
            val bundle = Bundle()
            bundle.putSerializable(MOVIE_KEY, it)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }

    }

    private fun observeLiveData() {
        viewModel?.UIstateLiveData?.observe(viewLifecycleOwner, Observer { state ->
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