package com.moodup.movies.utils.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.utils.adapter.viewholder.BaseViewHolder
import com.moodup.movies.utils.adapter.viewholder.movies.FooterViewHolder
import kotlinx.android.synthetic.main.movie_row.view.*

class MoviesAdapter() :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var movies = ArrayList<Movie?>()
    private var favouritesMoviesContainer = ArrayList<Movie?>()
    private var isProgressBarVisible = false
    var onItemClick: ((Movie) -> Unit)? = null

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
                holder.addToFavouriteListColorChange()
            } else {
                holder.removeFromFavouriteListColorChange()
            }
        }

    }

    inner class RowViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                movies[adapterPosition]?.let { it1 -> onItemClick?.invoke(it1) }

            }

            itemView.add_to_favourites_button?.setOnClickListener {
                addOrRemoveFromFavouritesList(movies[adapterPosition])
            }
        }

        private fun addOrRemoveFromFavouritesList(movie: Movie?) {
            if (!favouritesMoviesContainer.contains(movie)) {
                addToFavouritesList(movie)
            } else {
                removeFromFavouritesList(movie)
            }
        }

        private fun addToFavouritesList(movie: Movie?) {
            favouritesMoviesContainer.add(movie)
            addToFavouriteListColorChange()
            Toast.makeText(
                itemView.context,
                itemView.context.resources.getString(R.string.added),
                Toast.LENGTH_SHORT
            ).show()
        }

        fun addToFavouriteListColorChange() {
            itemView.add_to_favourites_button.text =
                itemView.context.resources.getString(R.string.remove_from_favourites)
            itemView.add_to_favourites_button.setBackgroundColor(Color.RED)
        }

        fun removeFromFavouriteListColorChange() {
            itemView.add_to_favourites_button.text =
                itemView.context.resources.getString(R.string.add_to_favourites)
            itemView.add_to_favourites_button.setBackgroundColor(
                itemView.context.resources.getColor(
                    R.color.green
                )
            )
        }

        private fun removeFromFavouritesList(movie: Movie?) {
            favouritesMoviesContainer.remove(movie)
            removeFromFavouriteListColorChange()
            Toast.makeText(
                itemView.context,
                itemView.context.resources.getString(R.string.removed),
                Toast.LENGTH_SHORT
            ).show()
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


    fun updateAdapter(_movies: List<Movie>) {
        hideFooterProgressBar()
        movies.addAll(_movies)
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