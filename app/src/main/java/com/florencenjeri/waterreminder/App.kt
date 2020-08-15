package com.florencenjeri.waterreminder

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.florencenjeri.waterreminder.database.UserSettingsDatabase
import com.florencenjeri.waterreminder.di.applicationModule
import com.florencenjeri.waterreminder.di.databaseModule
import com.florencenjeri.waterreminder.di.presenterModule
import com.florencenjeri.waterreminder.di.repositoryModule
import com.florencenjeri.waterreminder.workmanager.ReminderWorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {
        private lateinit var instance: App
        fun getAppContext(): Context = instance.applicationContext
        val database by lazy {
            UserSettingsDatabase.getDatabase(getAppContext())
        }
        val dao by lazy {
            database.userSettingsDao()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            //Declare my app context
            androidContext(this@App)
            //Declare all my D.I modules
            modules(
                presenterModule, databaseModule, applicationModule, repositoryModule
            )
        }
        scheduleWaterReminder()
    }

    //Background work should not delay app start
    fun scheduleWaterReminder() {
        val worker = buildWorker()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            ReminderWorkManager.WORKER_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            worker
        )
    }

    private fun buildWorker(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<ReminderWorkManager>(15, TimeUnit.SECONDS)
            .build()

    }

}