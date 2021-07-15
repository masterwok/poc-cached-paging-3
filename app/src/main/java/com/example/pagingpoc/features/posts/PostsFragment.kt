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
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagingpoc.PagingPocApp
import com.example.pagingpoc.databinding.FragmentPostsBinding
import com.example.pagingpoc.features.posts.adapters.PostAdapter
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class PostsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PostsViewModel by activityViewModels { viewModelFactory }

    private val userAdapter = PostAdapter()

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
        observeAdapter()
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun observeAdapter() = with(binding.swipeRefreshLayout) {
        lifecycleScope.launchWhenResumed {
            userAdapter
                .loadStateFlow
                .map { it.refresh }
                .collect {
                    isRefreshing = when (it) {
                        is LoadState.Error -> false
                        is LoadState.NotLoading -> false // !it.endOfPaginationReached
                        LoadState.Loading -> true
                    }
                }
        }
    }

    private fun observePagingFlow() {
        lifecycleScope.launch {
            viewModel.postFlow.collectLatest { pagingData ->
                userAdapter.submitData(pagingData)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

}