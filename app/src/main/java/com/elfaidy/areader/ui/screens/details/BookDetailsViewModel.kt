package com.elfaidy.areader.ui.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfaidy.areader.data.Resource
import com.elfaidy.areader.model.Item
import com.elfaidy.areader.model.MBook
import com.elfaidy.areader.repository.BookRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel() {

    var book: Resource<Item>? by mutableStateOf(null)
    private val fireStore = FirebaseFirestore.getInstance()
    var loading: Boolean by mutableStateOf(false)

    fun getABookById(bookId: String){
        viewModelScope.launch{
            try {
               book = repository.getBookInfo(bookId)
            }catch (e: Exception){}
        }
    }


    fun addABook(
        book: MBook,
        onComplete: () -> Unit
    ){

        val dbCollection = fireStore.collection("Books")
        loading = true
        dbCollection.add(book)
            .addOnSuccessListener { docRef ->
                val docId= docRef.id
                    dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            loading = false
                            onComplete()
                        }
                    }.addOnFailureListener {
                        loading = false
                        Log.d("Failure", "error update document id")
                    }
            }
    }


}