package com.miftah.moviecatalog.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miftah.moviecatalog.core.data.source.local.entity.MovieEntity
import com.miftah.moviecatalog.databinding.ItemMovieBinding

class AdapterCardMovies :
    PagingDataAdapter<MovieEntity, AdapterCardMovies.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnClickListener

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listMovieItem: MovieEntity) {
            Glide.with(binding.root)
                .load(listMovieItem.poster)
                .into(binding.itemImgMovie)
            binding.itemTitleMovie.text = listMovieItem.title
            binding.itemCategoryMovie.text = listMovieItem.type
            binding.itemYearMovie.text = listMovieItem.year
        }

        fun callCard(listStoryItem: MovieEntity) {
            onItemClickCallback.onClickCard(
                listStoryItem.id,
                listStoryItem.poster,
                listStoryItem.year
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                holder.callCard(item)
            }
        }
    }

    fun setOnClickCallback(call: OnClickListener) {
        this.onItemClickCallback = call
    }

    interface OnClickListener {
        fun onClickCard(id: String, posterUrl: String, year: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(
                oldItem: MovieEntity,
                newItem: MovieEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MovieEntity,
                newItem: MovieEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}