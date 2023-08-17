package com.techmobile.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.techmobile.artbooktesting.R
import com.techmobile.artbooktesting.api.RetrofitApi
import com.techmobile.artbooktesting.repo.ArtRepository
import com.techmobile.artbooktesting.repo.ArtRepositoryInterface
import com.techmobile.artbooktesting.roomdb.ArtDao
import com.techmobile.artbooktesting.roomdb.ArtsDatabase
import com.techmobile.artbooktesting.utils.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
    fun injectRoomDataBase( @ApplicationContext context: Context) =
        Room.databaseBuilder( context, ArtsDatabase::class.java,"ArtBookDB").build()

    @Singleton
    @Provides
    fun injectDao( database: ArtsDatabase ) = database.artDao()

    @Singleton
    @Provides
    fun injectRetrofitAPI() : RetrofitApi {
        return Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        ).baseUrl(BASE_URL).build().create(RetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun injectNormalRepo( dao : ArtDao, api : RetrofitApi ) = ArtRepository(dao, api) as ArtRepositoryInterface
    @Singleton
    @Provides
    fun injectGlide( @ApplicationContext context: Context ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions().placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground)
    )
}