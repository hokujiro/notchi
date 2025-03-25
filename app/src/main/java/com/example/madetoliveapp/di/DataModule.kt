package com.example.madetoliveapp.di

import android.content.Context
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
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
        val context: Context = get()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(context))// Add logging for debugging
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
    single<TaskApi> { get<Retrofit>().create(TaskApi::class.java) }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<RemoteMapper> { RemoteMapperImpl() }
}

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.encodedPath

        // Skip adding the token for login and register endpoints
        if (url.contains("/auth/login") || url.contains("/auth/register") || url.contains("/auth/google-login")) {
            return chain.proceed(originalRequest)
        }

        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)

        val newRequestBuilder = originalRequest.newBuilder()
        token?.let {
            newRequestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(newRequestBuilder.build())
    }
}