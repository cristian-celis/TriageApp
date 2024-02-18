package com.example.triagecol.di

import android.content.Context
import com.example.triagecol.presentation.admin.AdminViewModel
import com.example.triagecol.domain.usecases.RepositoryImpl
import com.example.triagecol.domain.datastore.DataStoreImpl
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

    private const val BASE_URL = "https://triage-api.onrender.com/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext appContext: Context): DataStoreImpl {
        return DataStoreImpl(appContext)
    }

    @Singleton
    @Provides
    fun provideRepositoryImpl(retrofit: Retrofit): RepositoryImpl {
        return RepositoryImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideViewModel(dataStoreImpl: DataStoreImpl, repositoryImpl: RepositoryImpl): AdminViewModel {
        return AdminViewModel(repositoryImpl)
    }
}