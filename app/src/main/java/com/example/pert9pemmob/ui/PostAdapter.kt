package com.example.pert9pemmob.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pert9pemmob.data.Post
import com.example.pert9pemmob.databinding.ItemPostBinding

/**
 * RecyclerView Adapter for displaying a list of Post items.
 */
class PostAdapter(
    private var posts: List<Post>,
    private val onPostClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        with(holder.binding) {
            tvPostIdBadge.text = "POST #${post.id}"
            tvUserId.text = "User #${post.userId}"
            tvTitle.text = post.title
            tvBody.text = post.body

            cardPost.setOnClickListener {
                onPostClick(post)
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}
