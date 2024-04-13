package com.elfaidy.areader.data


sealed class Resource<T>(val data: T? = null, val message: String? = null){
    class Loading<T>(data: T? = null): Resource<T>(data)
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String?, data: T? = null): Resource<T>(data, message )
}



/*data class DataOrException<T, Boolean, Exception>(
    var data: T? = null,
    var isLoading: Boolean? = null,
    var exception: Exception? = null
)*/
