package com.rtl.android.assignment.app.di

import android.content.Context
import androidx.room.Room
import com.rtl.android.assignment.api.di.NetworkModule
import com.rtl.android.assignment.app.MainApplication
import com.rtl.android.assignment.cache.database.AppDatabase
import com.rtl.android.assignment.cache.di.CacheModule
import com.rtl.android.assignment.repository.di.RepositoriesModule
import com.rtl.android.assignment.ui.activities.details.DetailsActivity
import com.rtl.android.assignment.ui.activities.list.MainActivity
import com.rtl.android.assignment.util.DispatcherModule
import com.rtl.android.assignment.viewmodel.di.ViewModelModule
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        NetworkModule::class,
        CacheModule::class,
        RepositoriesModule::class,
        ViewModelModule::class,
        AppModule::class,
        DispatcherModule::class
    ]
)
interface ApplicationComponent {
    fun inject(application: MainApplication)
    fun inject(activity: MainActivity)
    fun inject(activity: DetailsActivity)
}

@Module
class AppModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun providesAppDB(): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "weather"
    ).build()
}
