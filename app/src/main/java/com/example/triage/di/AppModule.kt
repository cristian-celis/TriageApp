package com.example.triage.di

import android.content.Context
import com.example.triage.data.remote.repositoriesImpl.StaffRepositoryImpl
import com.example.triage.data.local.datastore.DataStoreImpl
import com.example.triage.data.remote.repositoriesImpl.DoctorRepositoryImpl
import com.example.triage.data.remote.repositoriesImpl.MainRepositoryImpl
import com.example.triage.data.remote.repositoriesImpl.PatientRepositoryImpl
import com.example.triage.utils.EndPointConstants
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(EndPointConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideMainRepository(retrofit: Retrofit): MainRepositoryImpl {
        return MainRepositoryImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext appContext: Context): DataStoreImpl {
        return DataStoreImpl(appContext)
    }

    @Singleton
    @Provides
    fun provideStaffRepositoryImpl(retrofit: Retrofit): StaffRepositoryImpl {
        return StaffRepositoryImpl(retrofit)
    }

    @Singleton
    @Provides
    fun providePatientRepository(retrofit: Retrofit): PatientRepositoryImpl {
        return PatientRepositoryImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideDoctorRepository(retrofit: Retrofit): DoctorRepositoryImpl {
        return DoctorRepositoryImpl(retrofit)
    }
}