package com.sigortahatiratici.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sigortahatiratici.R
import com.sigortahatiratici.data.AppDatabase
import com.sigortahatiratici.data.SigortaTuru
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class HatirlatmaWorker(context: Context, params: WorkerParameters) : 
    CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        val dao = AppDatabase.getDatabase(applicationContext).aracDao()
        val araclar = dao.hatirlatmaAktifAraclariGetir()
        val bugun = LocalDate.now()
        val saat = LocalTime.now()
        
        // Sadece 09:00 veya 15:50 civarında bildirim gönder
        val uygunSaat = saat.hour == 9 || (saat.hour == 15 && saat.minute >= 50)
        
        if (uygunSaat) {
            araclar.forEach { arac ->
                val kalan = ChronoUnit.DAYS.between(bugun, arac.bitisTarihi)
                
                if (kalan in listOf(15L, 5L, 0L)) {
                    bildirimGoster(arac, kalan.toInt())
                }
            }
        }
        
        return Result.success()
    }
    
    private fun bildirimGoster(arac: com.sigortahatiratici.data.Arac, kalanGun: Int) {
        val context = applicationContext
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val kanal = NotificationChannel(
                "sigorta",
                "Sigorta Hatırlatmaları",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(kanal)
        }
        
        val mesaj = when (kalanGun) {
            15 -> "${arac.plaka} - 15 gün sonra bitiyor!"
            5 -> "⚠️ ${arac.plaka} - Son 5 gün!"
            0 -> "❌ ${arac.plaka} - BUGÜN bitiyor!"
            else -> ""
        }
        
        val tur = if (arac.sigortaTuru == SigortaTuru.TRAFIK_SIGORTASI) "Trafik" else "Kasko"
        
        val bildirim = NotificationCompat.Builder(context, "sigorta")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Sigorta Hatırlatıcı - $tur")
            .setContentText(mesaj)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        manager.notify(arac.plaka.hashCode(), bildirim)
    }
}
