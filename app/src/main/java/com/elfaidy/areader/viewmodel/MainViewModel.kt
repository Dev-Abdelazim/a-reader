package com.elfaidy.areader.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfaidy.areader.data.DataOrException
import com.elfaidy.areader.model.MBook
import com.elfaidy.areader.repository.FireRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FireRepository,
    private val context: Context
): ViewModel() {

    private val fireStore =  FirebaseFirestore.getInstance()

    val books: MutableState<DataOrException<List<MBook>, Boolean, Exception>>
         = mutableStateOf(DataOrException(emptyList(), true, Exception("")))
    val readingNowList: MutableState<List<MBook>> = mutableStateOf(emptyList())
    val haveReadList: MutableState<List<MBook>> = mutableStateOf(
        getBooksForCurrentUser().filter {
            it.startedReading != null && it.finishedReading != null
        }
    )

    val note: MutableState<String?> = mutableStateOf(null)
    val isStartReading: MutableState<Boolean> = mutableStateOf(false)
    val isFinish: MutableState<Boolean> = mutableStateOf(false)
    val isSelected: MutableState<Boolean> = mutableStateOf(false)
    val rating: MutableState<Int> = mutableStateOf(0)
    val ratingState = mutableStateOf(0)
    val firstName: MutableState<String> = mutableStateOf("")

    init {
        getAllBooksFromDatabase()
        getUserName()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            books.value.loading = true
            books.value = repository.getAllBooksFromDatabase()
            if (!books.value.data.isNullOrEmpty()) books.value.loading = false
        }
    }

    fun getBooksForCurrentUser(): List<MBook>{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var allBooks: List<MBook> = emptyList()
        if (!books.value.data.isNullOrEmpty()){
            allBooks =  books.value.data!!.filter {book ->
                book.userId == currentUser?.uid.toString()
            }
        }

        return allBooks
    }


    fun checkUpdateBook(book: MBook): Boolean{
        return book.notes != note.value
                || book.rating?.toInt() != rating.value
                || isStartReading.value
                || isFinish.value
    }


    fun updateBook(mBook: MBook){
        val startReading = if (isStartReading.value) Timestamp.now() else mBook.startedReading
        val finishedReading = if (isFinish.value) Timestamp.now() else mBook.finishedReading
        val book = hashMapOf(
            "notes" to note.value,
            "rating" to rating.value.toDouble(),
            "started_reading" to startReading,
            "finished_reading" to finishedReading
        ).toMap()

        fireStore.collection("Books")
            .document(mBook.id!!)
            .update(book)
            .addOnCompleteListener {
                showToast(context,"Update Book Successfully.")
            }
    }


    private fun showToast(context: Context , message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun deleteBook(bookId: String){
       fireStore
            .collection("Books")
            .document(bookId)
            .delete()
    }


    private fun getUserName(){
        fireStore
            .collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                  task
                      .result
                      .map { queryDocumentSnapshot ->
                          if (queryDocumentSnapshot.id == queryDocumentSnapshot.get("id")){
                              firstName.value = queryDocumentSnapshot.get("first_name").toString()
                              Log.d("112233", "firstName = ${firstName.value}")
                          }
                      }
                }
            }
    }
}