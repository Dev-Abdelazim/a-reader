package com.elfaidy.areader.repository

import com.elfaidy.areader.data.DataOrException
import com.elfaidy.areader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val query: Query
) {

    suspend fun getAllBooksFromDatabase()
      : DataOrException<List<MBook>, Boolean, Exception>{
        val books = DataOrException<List<MBook>, Boolean, Exception>()
        try {
            books.loading = true
            books.data = query
                .get()
                .await()
                .documents
                .map { documentSnapshot ->
                    documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!books.data.isNullOrEmpty()) books.loading = false

        }catch (e: FirebaseFirestoreException){
            books.exception = e
        }
        return books
    }

}