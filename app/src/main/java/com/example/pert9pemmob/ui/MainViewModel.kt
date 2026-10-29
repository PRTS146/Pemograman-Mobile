package com.example.pert9pemmob.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pert9pemmob.data.Post
import com.example.pert9pemmob.network.RetrofitClient
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class MainViewModel : ViewModel() {

    private val _postsState = MutableLiveData<UiState<List<Post>>>()
    val postsState: LiveData<UiState<List<Post>>> get() = _postsState

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        _postsState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getPosts()
                if (response.isSuccessful && response.body() != null) {
                    _postsState.value = UiState.Success(response.body()!!)
                } else {
                    _postsState.value = UiState.Error("Failed to fetch posts: ${response.message()}")
                }
            } catch (e: Exception) {
                _postsState.value = UiState.Error(e.localizedMessage ?: "Unknown network error occurred")
            }
        }
    }
}
