package com.example.madetoliveapp.di

import android.content.Context
import android.util.Log
import org.koin.dsl.module
import androidx.room.Room
import com.example.madetoliveapp.data.mapper.RemoteMapper
import com.example.madetoliveapp.data.mapper.RemoteMapperImpl
import com.example.madetoliveapp.data.repository.AuthRepository
import com.example.madetoliveapp.data.repository.AuthRepositoryImpl
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.data.repository.TaskRepositoryImpl
import com.example.madetoliveapp.data.source.local.bbdd.AppDatabase
import com.example.madetoliveapp.data.source.remote.api.TaskApi
import com.example.madetoliveapp.data.source.remote.auth.AuthApi
import com.example.madetoliveapp.presentation.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single { TokenManager(androidContext()) }

    // Room Database
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    //http client
    single(named("unauthenticated")) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    //http client
    single(named("authenticated")) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(TokenInterceptor(get()))
            .build()
    }

    // Retrofit without token (AuthApi)
    single(named("unauthenticatedRetrofit")) {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("unauthenticated")))
            .build()
    }

// Retrofit with token (TaskApi)
    single(named("authenticatedRetrofit")) {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("authenticated")))
            .build()
    }

    // DAO
    single { get<AppDatabase>().taskDao() }

    // AuthApi
    single<AuthApi> { get<Retrofit>(named("unauthenticatedRetrofit")).create(AuthApi::class.java) }
    single<TaskApi> { get<Retrofit>(named("authenticatedRetrofit")).create(TaskApi::class.java) }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<RemoteMapper> { RemoteMapperImpl() }
}

class TokenInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = tokenManager.getToken()
        Log.d("TokenInterceptor", "Token: $token")
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}