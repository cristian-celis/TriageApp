package com.example.triagecol.di

import android.content.Context
import com.example.triagecol.domain.usecases.StaffRepositoryImpl
import com.example.triagecol.domain.datastore.DataStoreImpl
import com.example.triagecol.domain.usecases.DoctorRepository
import com.example.triagecol.domain.usecases.LoginRepository
import com.example.triagecol.domain.usecases.MainRepository
import com.example.triagecol.domain.usecases.PatientRepository
import com.example.triagecol.utils.EndPointConstants
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
    fun provideMainRepository(retrofit: Retrofit): MainRepository{
        return MainRepository(retrofit)
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
    fun providePatientRepository(retrofit: Retrofit): PatientRepository{
        return PatientRepository(retrofit)
    }

    @Singleton
    @Provides
    fun provideDoctorRepository(retrofit: Retrofit): DoctorRepository{
        return DoctorRepository(retrofit)
    }
}