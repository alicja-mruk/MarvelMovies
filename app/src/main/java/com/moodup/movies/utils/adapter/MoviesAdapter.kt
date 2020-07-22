package com.moodup.movies.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.moodup.movies.model.Movie
import kotlinx.android.synthetic.main.movie_row.view.*
import java.util.*

class MoviesAdapter(private val _movies: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies = _movies

    var onItemClick: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.movie_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        holder.bindData(movies[position])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(movies[adapterPosition])
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

    fun setAdapter(_movies:List<Movie>){
        movies = _movies
        notifyDataSetChanged()

    }

}