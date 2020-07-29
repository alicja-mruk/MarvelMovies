package com.moodup.movies.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
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
import com.example.movies.databinding.FragmentHomeBinding
import com.jakewharton.rxbinding2.widget.queryTextChanges
import com.moodup.movies.state.UIState
import com.moodup.movies.ui.details.DetailsFragment.Companion.MOVIE_KEY
import com.moodup.movies.ui.home.adapter.HomeAdapter
import com.moodup.movies.viewmodel.home.HomeViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var viewModel: HomeViewModel? = null
    private var adapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(HomeViewModel::class.java)
        }

        setUpSearchView()
        setUpRecyclerView()
        setUpAdapter()
        observeLiveData()

    }

    private fun setUpRecyclerView() {
        linearLayoutManager = LinearLayoutManager(activity)
        binding.moviesRecyclerView.layoutManager = linearLayoutManager

        binding.moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount: Int = linearLayoutManager.itemCount
                val lastVisibleItem: Int = linearLayoutManager.findLastVisibleItemPosition()

                viewModel?.let {
                    if (!it.isDataLoading && lastVisibleItem == totalItemCount - 1) {
                        it.isDataLoading = true

                        if (it.checkIfThereIsScrollingPossible(totalItemCount)) {
                            adapter?.showFooterProgressBar()
                            it.getMovies(totalItemCount + 1, binding.movieSearchview.query.toString())
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun setUpSearchView() {
        binding.movieSearchview.clearFocus()

        binding.movieSearchview.queryTextChanges().skip(2)
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
            adapter = HomeAdapter(viewModel, this)
            binding.moviesRecyclerView.adapter = adapter
            binding.moviesRecyclerView.addItemDecoration(
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
        binding.moviesRecyclerView.visibility = View.GONE
        binding.resultsTextView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showOnError() {
        binding.moviesRecyclerView.visibility = View.GONE
        binding.resultsTextView.visibility = View.VISIBLE
        binding.resultsTextView.text = R.string.error.toString()
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.moviesRecyclerView.visibility = View.GONE
        binding.resultsTextView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.moviesRecyclerView.visibility = View.VISIBLE
        binding.resultsTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }


}