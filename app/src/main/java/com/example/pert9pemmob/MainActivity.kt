package com.example.pert9pemmob

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pert9pemmob.databinding.ActivityMainBinding
import com.example.pert9pemmob.ui.MainViewModel
import com.example.pert9pemmob.ui.PostAdapter
import com.example.pert9pemmob.ui.UiState

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.posts_title)
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(emptyList()) { post ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_POST_ID, post.id)
            }
            startActivity(intent)
            // Smooth transition animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
            // Schedule layout animation
            scheduleLayoutAnimation()
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.fetchPosts()
        }
    }

    private fun observeViewModel() {
        viewModel.postsState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                    binding.recyclerViewPosts.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    binding.recyclerViewPosts.visibility = View.VISIBLE

                    postAdapter.updatePosts(state.data)
                    binding.recyclerViewPosts.scheduleLayoutAnimation()
                }
                is UiState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.recyclerViewPosts.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                }
            }
        }
    }
}
