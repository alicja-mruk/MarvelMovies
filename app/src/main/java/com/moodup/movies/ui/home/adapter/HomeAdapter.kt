package com.moodup.movies.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.ui.home.adapter.viewholder.BaseViewHolder
import com.moodup.movies.ui.home.adapter.viewholder.movies.FooterViewHolder
import com.moodup.movies.viewmodel.home.HomeViewModel
import kotlinx.android.synthetic.main.movie_row.view.*

class HomeAdapter(viewModel: HomeViewModel, fragment: Fragment) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var movies = ArrayList<Movie?>()
    private var isProgressBarVisible = false
    var onItemClick: ((Movie) -> Unit)? = null


    init {
        viewModel.movieLiveData.observe(fragment.viewLifecycleOwner, Observer {
            viewModel.isDataLoading = false
            hideFooterProgressBar()
            movies.clear()
            movies.addAll(it)
            notifyDataSetChanged()
        })

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FOOTER -> {
                FooterViewHolder(
                    inflater.inflate(R.layout.footer_progressbar, parent, false)
                )
            }

            TYPE_LAYOUT -> {
                RowViewHolder(inflater.inflate(R.layout.movie_row, parent, false))
            }

            else -> RowViewHolder(inflater.inflate(R.layout.movie_row, parent, false))
        }

    }

    override fun getItemCount(): Int = movies.size


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is RowViewHolder) {
            movies[position]?.let { holder.bindData(it) }
        }

    }

    inner class RowViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                movies[adapterPosition]?.let { it1 -> onItemClick?.invoke(it1) }
            }
        }

        fun bindData(item: Movie) {
            with(itemView) {
                Glide.with(context)
                    .load("${item.thumbnail.path}.${item.thumbnail.extension}")
                    .into(movie_picture)
                movie_title.text = item.title
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) {
            TYPE_FOOTER
        } else TYPE_LAYOUT
    }

    private fun isPositionFooter(position: Int): Boolean {
        return movies[position] == null
    }

    fun showFooterProgressBar() {
        isProgressBarVisible = true
        movies.add(null)
        notifyItemInserted(movies.size - 1)
    }

    private fun hideFooterProgressBar() {
        isProgressBarVisible = false
        val position = movies.size - 1
        if (position >= 0) {
            movies.removeAt(position)
            notifyDataSetChanged()
        }
    }



    companion object {
        const val TYPE_FOOTER: Int = 0
        const val TYPE_LAYOUT: Int = 1
    }

}