package com.rtl.android.assignment.app.di

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.room.Room
import com.rtl.android.assignment.api.di.NetworkModule
import com.rtl.android.assignment.app.MainApplication
import com.rtl.android.assignment.cache.database.AppDatabase
import com.rtl.android.assignment.cache.di.CacheModule
import com.rtl.android.assignment.repository.di.RepositoriesModule
import com.rtl.android.assignment.ui.activities.list.MainActivity
import com.rtl.android.assignment.util.DispatcherModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@ExperimentalFoundationApi
@Singleton
@Component(
    modules = [
        NetworkModule::class,
        CacheModule::class,
        RepositoriesModule::class,
        AppModule::class,
        DispatcherModule::class
    ]
)
interface ApplicationComponent {
    fun inject(application: MainApplication)
    fun inject(activity: MainActivity)
}

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesAppDB(@ApplicationContext appContext: Context): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "weather"
    ).build()
}
