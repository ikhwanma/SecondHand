package binar.lima.satu.secondhand.di

import android.content.Context
import androidx.room.Room
import binar.lima.satu.secondhand.data.local.datastore.DataStoreManager
import binar.lima.satu.secondhand.data.local.room.ProductDao
import binar.lima.satu.secondhand.data.local.room.ProductDatabase
import binar.lima.satu.secondhand.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://market-final-project.herokuapp.com/"

    private val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    @Provides
    @Singleton
    fun providesRetrofit(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesPref(@ApplicationContext appContext: Context) : DataStoreManager =  DataStoreManager(appContext)

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext appContext: Context): ProductDatabase{
        return Room.databaseBuilder(appContext, ProductDatabase::class.java, "product.db").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesInstanceRoom(productDatabase: ProductDatabase): ProductDao = productDatabase.productDao()
}