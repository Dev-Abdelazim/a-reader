package com.elfaidy.areader.ui.screens.login

data class LoadingState(
    val status: Status,
    val message: String? = null
){

    companion object{
        val LOADING = LoadingState(Status.LOADING)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val FAILED = LoadingState(Status.FAILED)
        val IDLE = LoadingState(Status.IDLE)
    }


    enum class Status {
        LOADING,
        SUCCESS,
        FAILED,
        IDLE;
    }

}
