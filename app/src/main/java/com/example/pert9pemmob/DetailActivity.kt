package com.example.pert9pemmob

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pert9pemmob.databinding.ActivityDetailBinding
import com.example.pert9pemmob.ui.DetailViewModel
import com.example.pert9pemmob.ui.UiState

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    private var postId: Int = 1

    companion object {
        const val EXTRA_POST_ID = "extra_post_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getIntExtra(EXTRA_POST_ID, 1)

        setupToolbar()
        setupListeners()
        observeViewModel()

        viewModel.fetchPostById(postId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListeners() {
        binding.swipeRefreshDetail.setOnRefreshListener {
            viewModel.fetchPostById(postId)
        }
        binding.btnDetailRetry.setOnClickListener {
            viewModel.fetchPostById(postId)
        }
    }

    private fun observeViewModel() {
        viewModel.postState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.swipeRefreshDetail.isRefreshing = false
                    binding.shimmerDetailContainer.visibility = View.VISIBLE
                    binding.shimmerDetailContainer.startShimmer()
                    binding.scrollViewDetail.visibility = View.GONE
                    binding.layoutDetailError.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.swipeRefreshDetail.isRefreshing = false
                    binding.shimmerDetailContainer.stopShimmer()
                    binding.shimmerDetailContainer.visibility = View.GONE
                    binding.layoutDetailError.visibility = View.GONE
                    binding.scrollViewDetail.visibility = View.VISIBLE

                    val post = state.data
                    binding.tvDetailPostIdBadge.text = getString(R.string.post_id_format, post.id)
                    binding.tvDetailUserId.text = getString(R.string.user_id_format, post.userId)
                    binding.tvDetailTitle.text = post.title
                    binding.tvDetailBody.text = post.body
                }
                is UiState.Error -> {
                    binding.swipeRefreshDetail.isRefreshing = false
                    binding.shimmerDetailContainer.stopShimmer()
                    binding.shimmerDetailContainer.visibility = View.GONE
                    binding.scrollViewDetail.visibility = View.GONE
                    binding.layoutDetailError.visibility = View.VISIBLE
                }
            }
        }
    }
}
