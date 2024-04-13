package com.elfaidy.areader.ui.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfaidy.areader.data.Resource
import com.elfaidy.areader.model.Item
import com.elfaidy.areader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val repo: BookRepository
): ViewModel() {

    var books: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    val search: MutableState<String> = mutableStateOf("")


    init {
        searchBooks("Jetpack Compose")
    }

    fun searchBooks(query: String){
        viewModelScope.launch {
            if (query.isEmpty()){
                return@launch
            }

            try {
                when(val response = repo.getBooks(query)){
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("Network", "searchBooks: Failed getting books", )
                    }
                    is Resource.Success -> {
                        books = response.data!!
                        if (books.isNotEmpty()) isLoading = false
                    }
                    else -> {isLoading = false}
                }
            }catch (e: Exception){
                isLoading = false
                Log.d("Network", "searchBooks: ${e.message.toString()}")
            }
        }
    }



}