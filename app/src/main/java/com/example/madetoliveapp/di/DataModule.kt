package com.example.madetoliveapp.di

import org.koin.dsl.module
import androidx.room.Room
import com.example.madetoliveapp.data.mapper.RemoteMapper
import com.example.madetoliveapp.data.mapper.RemoteMapperImpl
import com.example.madetoliveapp.data.repository.AuthRepository
import com.example.madetoliveapp.data.repository.AuthRepositoryImpl
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.data.repository.TaskRepositoryImpl
import com.example.madetoliveapp.data.source.local.bbdd.AppDatabase
import com.example.madetoliveapp.data.source.remote.auth.AuthApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    // Room Database
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    //http client
    single {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging) // Add logging for debugging
            .build()
    }

    //Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Replace with your base URL
            .addConverterFactory(GsonConverterFactory.create()) // Or other converter
            .client(get())
            .build()

    }

    // DAO
    single { get<AppDatabase>().taskDao() }

    // AuthApi
    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<RemoteMapper> { RemoteMapperImpl() }
}