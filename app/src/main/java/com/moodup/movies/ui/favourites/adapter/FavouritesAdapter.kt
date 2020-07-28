package com.moodup.movies.ui.favourites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.ui.home.adapter.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.card_view_favourites.view.*

class FavouritesAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var onItemClick: ((Movie) -> Unit)? = null
    private var data = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavouriteMovieViewHolder(
            inflater.inflate(
                R.layout.card_view_favourites,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is FavouriteMovieViewHolder) {
            data[position].let { holder.bindData(it) }
        }

    }

    inner class FavouriteMovieViewHolder(itemView: View) : BaseViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                data[adapterPosition].let { it1 -> onItemClick?.invoke(it1) }

            }
        }

        fun bindData(item: Movie) {
            with(itemView) {
                Glide.with(context)
                    .load("${item.thumbnail.path}.${item.thumbnail.extension}")
                    .into(movie_favourites_picture)
                movie_favourites_title.text = item.title
            }
        }
    }

    fun setData(list: List<Movie>) {
        data = list as ArrayList<Movie>
        notifyDataSetChanged()
    }
}