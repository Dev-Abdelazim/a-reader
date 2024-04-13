package com.elfaidy.areader.repository

import com.elfaidy.areader.data.Resource
import com.elfaidy.areader.model.Item
import com.elfaidy.areader.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val api: BooksApi
) {


    suspend fun getBooks(searchQuery: String)
     : Resource<List<Item>>{
        return try{
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        }catch (e: Exception){
            Resource.Error(message = e.message.toString())
        }
    }


    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return try {
            Resource.Loading(data = true)
            val item = api.getBookInfo(bookId)
            if (item.toString().isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = item)
        }catch (e: Exception){
            Resource.Error(message = e.message.toString())
        }
    }

}