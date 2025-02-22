package com.elfaidy.areader.data

data class DataOrException<T, Boolean, E:Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var exception: E? = null
)
