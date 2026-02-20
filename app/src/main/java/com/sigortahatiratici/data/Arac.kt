package com.sigortahatiratici.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class SigortaTuru {
    TRAFIK_SIGORTASI,
    KASKO
}

@Entity(tableName = "araclar")
data class Arac(
    @PrimaryKey
    val plaka: String,
    val sigortaTuru: SigortaTuru,
    val bitisTarihi: LocalDate,
    val hatirlatmaAktif: Boolean = true
)
