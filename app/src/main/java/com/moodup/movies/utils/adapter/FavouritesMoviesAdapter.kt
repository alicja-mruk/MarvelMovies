package com.moodup.movies.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import com.moodup.movies.utils.adapter.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.card_view_favourites.view.*
import kotlinx.android.synthetic.main.movie_row.view.*

class FavouritesMoviesAdapter : RecyclerView.Adapter<BaseViewHolder>(){
    private var favouritesMovies = listOf<Movie?>()
    fun setData(movies:List<Movie?>){
        favouritesMovies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardViewHolder(inflater.inflate(R.layout.card_view_favourites, parent, false))
    }

    override fun getItemCount(): Int {
       return favouritesMovies.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is FavouritesMoviesAdapter.CardViewHolder) {
            favouritesMovies[position]?.let { holder.bindData(it) }
        }
    }


    inner class CardViewHolder(itemView: View) : BaseViewHolder(itemView) {

        fun bindData(item: Movie) {
            with(itemView) {
                Glide.with(context)
                    .load("${item.thumbnail.path}.${item.thumbnail.extension}")
                    .into(movie_favourites_picture)
                movie_favourites_title.text = item.title
            }
        }
    }
}