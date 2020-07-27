package com.moodup.movies.utils.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.ui.home.HomeFragment
import com.moodup.movies.utils.adapter.viewholder.BaseViewHolder
import com.moodup.movies.utils.adapter.viewholder.movies.FooterViewHolder
import com.moodup.movies.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.movie_row.view.*
import java.util.Collections.addAll

class MoviesAdapter(viewModel: MovieViewModel, fragment: Fragment) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var movies = ArrayList<Movie?>()
    private var favouritesMoviesContainer = ArrayList<Movie?>()
    private var isProgressBarVisible = false
    var onItemClick: ((Movie) -> Unit)? = null
    var onFavouritesButtonClick: ((Movie) -> Unit)? = null


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

            if (favouritesMoviesContainer.contains(movies[position])) {
                holder.addedToFavouritesColorChange()
            } else {
                holder.removedFromFavouritesColorChange()
            }
        }

    }

    inner class RowViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                movies[adapterPosition]?.let { it1 -> onItemClick?.invoke(it1) }

            }

            itemView.add_to_favourites_button?.setOnClickListener {
                if (favouritesMoviesContainer.contains(movies[adapterPosition])) {
                    addedToFavouritesColorChange()
                } else {
                    removedFromFavouritesColorChange()
                }

                movies[adapterPosition]?.let { it2 ->
                    onFavouritesButtonClick?.invoke(it2)
                }
            }

        }

        fun addedToFavouritesColorChange() {
            itemView.add_to_favourites_button.text =
                itemView.context.resources.getString(R.string.remove_from_favourites)
            itemView.add_to_favourites_button.setBackgroundColor(Color.RED)
        }

        fun removedFromFavouritesColorChange() {
            itemView.add_to_favourites_button.text =
                itemView.context.resources.getString(R.string.add_to_favourites)
            itemView.add_to_favourites_button.setBackgroundColor(
                itemView.context.resources.getColor(
                    R.color.green
                )
            )
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

    fun updateFavouritesList(_favourites: List<Movie?>) {
        favouritesMoviesContainer = _favourites as ArrayList<Movie?>
        notifyDataSetChanged()
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

    fun clearMoviesList() {
        movies.clear()
    }


    companion object {
        const val TYPE_FOOTER: Int = 0
        const val TYPE_LAYOUT: Int = 1
    }

}