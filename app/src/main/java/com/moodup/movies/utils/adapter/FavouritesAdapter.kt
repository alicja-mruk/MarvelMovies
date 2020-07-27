package com.moodup.movies.utils.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moodup.movies.model.Movie
import com.moodup.movies.utils.adapter.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.movie_row.view.*
import kotlinx.android.synthetic.main.movie_row.view.movie_title

class FavouritesAdapter() :RecyclerView.Adapter<BaseViewHolder>() {

    private var favouritesMovies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = favouritesMovies.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if(holder is FavouriteMovieViewHolder){
            favouritesMovies[position].let { holder.bindData(it) }
        }

    }

    inner class FavouriteMovieViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bindData(item: Movie) {
            with(itemView) {
                Glide.with(context)
                    .load("${item.thumbnail.path}.${item.thumbnail.extension}")
                    .into(movie_picture)
                movie_title.text = item.title
            }
        }
    }
}