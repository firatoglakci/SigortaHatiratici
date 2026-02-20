package com.sigortahatiratici

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sigortahatiratici.worker.HatirlatmaWorker
import java.util.concurrent.TimeUnit

class SigortaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        val workRequest = PeriodicWorkRequestBuilder<HatirlatmaWorker>(1, TimeUnit.DAYS)
            .build()
            
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "sigorta_hatirlatma",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
