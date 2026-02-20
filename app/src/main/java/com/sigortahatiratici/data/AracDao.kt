package com.sigortahatiratici.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AracDao {
    @Query("SELECT * FROM araclar ORDER BY bitisTarihi ASC")
    fun tumAraclariGetir(): Flow<List<Arac>>
    
    @Query("SELECT * FROM araclar WHERE plaka = :plaka")
    suspend fun aracGetir(plaka: String): Arac?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun aracEkle(arac: Arac)
    
    @Delete
    suspend fun aracSil(arac: Arac)
    
    @Query("SELECT * FROM araclar WHERE hatirlatmaAktif = 1")
    suspend fun hatirlatmaAktifAraclariGetir(): List<Arac>
}
