package com.example.pagingpoc.features.posts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingpoc.R
import com.example.pagingpoc.common.contracts.Configurable
import com.example.pagingpoc.databinding.ViewLoadStateBinding

class PostsLoadStateAdapter : LoadStateAdapter<PostsLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewHolder = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_load_state, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.configure(loadState)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), Configurable<LoadState> {

        private val binding = ViewLoadStateBinding.bind(view)

        override fun configure(model: LoadState) = with(binding) {
//            binding.textViewId.text = model.id.toString()
//            binding.textViewTitle.text = model.title
//            binding.textViewBody.text = model.body
        }
    }
}