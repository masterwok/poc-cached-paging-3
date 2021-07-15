package com.example.pagingpoc.features.posts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingpoc.R
import com.example.pagingpoc.common.contracts.Configurable
import com.example.pagingpoc.data.models.Post
import com.example.pagingpoc.databinding.ViewHolderPostBinding


class PostAdapter : PagingDataAdapter<Post, PostAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { user -> holder.configure(user) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_holder_post, parent, false)
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), Configurable<Post> {

        private val binding = ViewHolderPostBinding.bind(view)

        override fun configure(model: Post) = with(binding) {
            binding.textViewId.text = model.id.toString()
            binding.textViewTitle.text = model.title
            binding.textViewBody.text = model.body
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
            override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        }
    }
}