package com.miftah.moviecatalog.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.miftah.moviecatalog.core.data.domain.Bookmark
import com.miftah.moviecatalog.databinding.ItemBookmarkBinding

class AdapterCardBookmark(private val options: FirebaseRecyclerOptions<Bookmark>) : FirebaseRecyclerAdapter<Bookmark, AdapterCardBookmark.BookmarkViewHolder>(options) {

    private lateinit var onItemClickCallback: OnClickListener

    inner class BookmarkViewHolder(private val binding : ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(bookmark : Bookmark) {
            binding.itemBookmarksTitle.text = bookmark.title
            binding.itemBookmarksYear.text = bookmark.year
            Glide.with(itemView.context)
                .load(bookmark.poster)
                .circleCrop()
                .into(binding.itemBookmarksImg)
        }

        fun callCard(bookmark: Bookmark) {
            onItemClickCallback.onClickCard(
                bookmark.mbdId,
                bookmark.poster,
                bookmark.year
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int, model: Bookmark) {
        holder.bind(model)
        holder.itemView.setOnClickListener {
            holder.callCard(model)
        }
    }

    fun setOnClickCallback(call: OnClickListener) {
        this.onItemClickCallback = call
    }

    interface OnClickListener {
        fun onClickCard(id: String?, posterUrl: String?, year: String?)
    }
}