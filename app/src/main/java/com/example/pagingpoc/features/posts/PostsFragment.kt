package com.example.pagingpoc.features.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingpoc.PagingPocApp
import com.example.pagingpoc.databinding.FragmentPostsBinding
import com.example.pagingpoc.features.posts.adapters.PostAdapter
import com.example.pagingpoc.features.posts.adapters.PostsLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PostsViewModel by activityViewModels { viewModelFactory }

    private val loadStateAdapter = PostsLoadStateAdapter()

    private val postAdapter = PostAdapter().apply {
        withLoadStateFooter(loadStateAdapter)
    }

    private val binding get() = _binding!!

    private var _binding: FragmentPostsBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as PagingPocApp)
            .appComponent
            .portfolioComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observePagingFlow()
        subscribeToViewComponents()
    }

    private fun subscribeToViewComponents() {
        subscribeToSwipeRefreshLayout()
    }

    private fun subscribeToSwipeRefreshLayout() = with(binding) {
        swipeRefreshLayout.setOnRefreshListener {
            postAdapter.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observePagingFlow() {
        lifecycleScope.launch {
            viewModel.postFlow.collectLatest { pagingData ->
                postAdapter.submitData(pagingData)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = ConcatAdapter(postAdapter, loadStateAdapter)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

}