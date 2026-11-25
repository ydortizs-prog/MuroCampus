package com.ortiz.murocampus.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ortiz.murocampus.data.PostRepositoryRemote
import com.ortiz.murocampus.data.remote.RetrofitClient

// Crea el PostsViewModel con su Repository (el ViewModel solo ve la interface)
val PostsViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        PostsViewModel(PostRepositoryRemote(RetrofitClient.api))
    }
}
