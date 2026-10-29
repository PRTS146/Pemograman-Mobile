package com.example.pert9pemmob.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pert9pemmob.data.Post
import com.example.pert9pemmob.network.RetrofitClient
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _postState = MutableLiveData<UiState<Post>>()
    val postState: LiveData<UiState<Post>> get() = _postState

    fun fetchPostById(id: Int) {
        _postState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getPostById(id)
                if (response.isSuccessful && response.body() != null) {
                    _postState.value = UiState.Success(response.body()!!)
                } else {
                    _postState.value = UiState.Error("Failed to fetch post details: ${response.message()}")
                }
            } catch (e: Exception) {
                _postState.value = UiState.Error(e.localizedMessage ?: "Unknown network error occurred")
            }
        }
    }
}
