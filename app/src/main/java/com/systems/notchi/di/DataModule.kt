package com.systems.notchi.di

import android.util.Log
import org.koin.dsl.module
import androidx.room.Room
import com.systems.notchi.BuildConfig
import com.systems.notchi.data.mapper.RemoteMapper
import com.systems.notchi.data.mapper.RemoteMapperImpl
import com.systems.notchi.data.repository.auth.AuthRepository
import com.systems.notchi.data.repository.auth.AuthRepositoryImpl
import com.systems.notchi.data.repository.frames.FrameRepository
import com.systems.notchi.data.repository.frames.FrameRepositoryImpl
import com.systems.notchi.data.repository.projects.ProjectRepository
import com.systems.notchi.data.repository.projects.ProjectRepositoryImpl
import com.systems.notchi.data.repository.rewards.RewardRepository
import com.systems.notchi.data.repository.rewards.RewardRepositoryImpl
import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.data.repository.tasks.TaskRepositoryImpl
import com.systems.notchi.data.repository.user.UserRepository
import com.systems.notchi.data.repository.user.UserRepositoryImpl
import com.systems.notchi.data.source.local.bbdd.AppDatabase
import com.systems.notchi.data.source.remote.api.ProjectApi
import com.systems.notchi.data.source.remote.api.RewardApi
import com.systems.notchi.data.source.remote.api.TaskApi
import com.systems.notchi.data.source.remote.api.UserApi
import com.systems.notchi.data.source.remote.auth.AuthApi
import com.systems.notchi.data.source.remote.auth.RefreshTokenRequest
import com.systems.notchi.presentation.auth.TokenManager
import kotlinx.coroutines.runBlocking
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

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single(named("unauthenticated")) {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC // or HEADERS
        }
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single(named("authenticated")) {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(get(), get())) // ✅ Token logic first
            .addInterceptor(logging)                        // ✅ Logging last
            .build()
    }

    single(named("unauthenticatedRetrofit")) {
        Retrofit.Builder()
            //.baseUrl("http://10.0.2.2:8080/")
             .baseUrl("https://server-1son.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("unauthenticated")))
            .build()
    }

    single(named("authenticatedRetrofit")) {
        Retrofit.Builder()
           //.baseUrl("http://10.0.2.2:8080/")
            .baseUrl("https://server-1son.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get(named("authenticated")))
            .build()
    }

    // DAO
    single { get<AppDatabase>().taskDao() }

    // AuthApi
    single<AuthApi> { get<Retrofit>(named("unauthenticatedRetrofit")).create(AuthApi::class.java) }
    single<TaskApi> { get<Retrofit>(named("authenticatedRetrofit")).create(TaskApi::class.java) }
    single<UserApi> { get<Retrofit>(named("authenticatedRetrofit")).create(UserApi::class.java) }
    single<ProjectApi> { get<Retrofit>(named("authenticatedRetrofit")).create(ProjectApi::class.java) }
    single<RewardApi> { get<Retrofit>(named("authenticatedRetrofit")).create(RewardApi::class.java) }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<ProjectRepository> { ProjectRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<FrameRepository> { FrameRepositoryImpl(get(), get()) }
    single<RewardRepository> { RewardRepositoryImpl(get(), get()) }
    single<RemoteMapper> { RemoteMapperImpl() }
}

class TokenInterceptor(
    private val tokenManager: TokenManager,
    private val authApi: AuthApi // ✅ Inject AuthApi (unauthenticated)
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val accessToken = tokenManager.getAccessToken()
        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        val response = chain.proceed(requestBuilder.build())

        // If 401 — try refreshing token
        if (response.code == 401) {
            response.close() // 👈 Important to close the original response
            val refreshToken = tokenManager.getRefreshToken()

            if (!refreshToken.isNullOrEmpty()) {
                try {
                    val refreshResponse = runBlocking {
                        authApi.refreshToken(RefreshTokenRequest(refreshToken))
                    }

                    if (refreshResponse.isSuccessful) {
                        val newTokens = refreshResponse.body()
                        if (newTokens != null) {
                            tokenManager.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                            val newRequest = originalRequest.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer ${newTokens.accessToken}")
                                .build()

                            val newResponse = chain.proceed(newRequest)

                            // ✅ Buffer the body so Retrofit can read it
                            val bufferedBody = newResponse.peekBody(Long.MAX_VALUE)
                            return newResponse.newBuilder().body(bufferedBody).build()
                        }
                    }

                } catch (e: Exception) {
                    Log.e("TokenInterceptor", "Token refresh failed: ${e.message}")
                }
            }

            // Refresh failed → logout
            tokenManager.clearTokens()
            tokenManager.notifySessionExpired()
        }

        return response
    }
}