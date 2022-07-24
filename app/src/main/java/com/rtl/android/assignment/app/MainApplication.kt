package com.rtl.android.assignment.app

import android.app.Application
import com.rtl.android.assignment.api.di.NetworkModule
import com.rtl.android.assignment.app.di.AppModule
import com.rtl.android.assignment.app.di.ApplicationComponent
import com.rtl.android.assignment.app.di.DaggerApplicationComponent
import com.rtl.android.assignment.cache.di.CacheModule
import com.rtl.android.assignment.repository.di.RepositoriesModule


class MainApplication : Application() {
    var appComponent: ApplicationComponent = DaggerApplicationComponent
        .builder()
        .repositoriesModule(RepositoriesModule())
        .cacheModule(CacheModule())
        .networkModule(NetworkModule())
        .appModule(AppModule(this))
        .build()


    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}