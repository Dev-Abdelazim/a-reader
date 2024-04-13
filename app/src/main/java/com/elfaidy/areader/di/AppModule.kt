package com.elfaidy.areader.di

import android.content.Context
import com.elfaidy.areader.network.BooksApi
import com.elfaidy.areader.repository.BookRepository
import com.elfaidy.areader.repository.FireRepository
import com.elfaidy.areader.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookApi(): BooksApi
      = Retrofit.Builder()
        .baseUrl(Constants.BASE_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BooksApi::class.java)


    @Singleton
    @Provides
    fun provideRepository(api: BooksApi): BookRepository
       = BookRepository(api)


    @Singleton
    @Provides
    fun provideFireRepository(): FireRepository
      = FireRepository(
          query = FirebaseFirestore
              .getInstance()
              .collection("Books")
      )


    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context
      = context
}