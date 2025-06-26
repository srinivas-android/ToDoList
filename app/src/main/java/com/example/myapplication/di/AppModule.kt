package com.example.myapplication.di

import android.content.Context
import coil.ImageLoader
import androidx.room.Room
import com.example.myapplication.data.source.local.TaskDao
import com.example.myapplication.data.source.local.TaskDatabase
import com.example.myapplication.data.source.remote.SetTaskApi
import com.example.myapplication.util.constants.Constants
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

    @Provides
    @Singleton
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit:Retrofit):SetTaskApi{
        return retrofit.create(SetTaskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context:Context):TaskDatabase =
        Room.databaseBuilder(context,TaskDatabase::class.java,"taskDatabase")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(database:TaskDatabase):TaskDao{
        return database.getTaskFromDao()
    }
    @Provides
    @Singleton // Ensures only one instance of ImageLoader is created
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            // Add any custom Coil configuration here, for example:
            // .crossfade(true)
            // .placeholder(R.drawable.image_placeholder)
            // .error(R.drawable.image_error)
            .build()
    }
}