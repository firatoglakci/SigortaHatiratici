package com.sigortahatiratici.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sigortahatiratici.data.SigortaTuru
import com.sigortahatiratici.ui.theme.*
import com.sigortahatiratici.viewmodel.AracViewModel
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AracDetay(navController: NavController, plaka: String) {
    val viewModel: AracViewModel = viewModel()
    val araclar by viewModel.tumAraclar.collectAsState(initial = emptyList())
    val arac = araclar.find { it.plaka == plaka }
    
    var silDialog by remember { mutableStateOf(false) }
    
    if (arac == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }
    
    val kalan = ChronoUnit.DAYS.between(java.time.LocalDate.now(), arac.bitisTarihi).toInt()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detay", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaviPrimary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("arac_ekle/${arac.plaka}") }) {
                        Icon(Icons.Default.Edit, "Düzenle", tint = Color.White)
                    }
                    IconButton(onClick = { silDialog = true }) {
                        Icon(Icons.Default.Delete, "Sil", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaviPrimary)
            ) {
                Text(
                    arac.plaka,
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            BilgiKart("Tür", if (arac.sigortaTuru == SigortaTuru.TRAFIK_SIGORTASI) "Trafik Sigortası" else "Kasko")
            BilgiKart("Bitiş", arac.bitisTarihi.toString())
            BilgiKart("Kalan Gün", "$kalan gün")
            
            val durum = when {
                kalan < 0 -> "❌ SÜRESİ DOLDU"
                kalan <= 5 -> "⚠️ SON 5 GÜN"
                kalan <= 15 -> "⏰ YAKLAŞIYOR"
                else -> "✅ AKTİF"
            }
            BilgiKart("Durum", durum)
            
            Card(colors = CardDefaults.cardColors(containerColor = MaviTertiary)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Hatırlatma Ayarları", fontWeight = FontWeight.Bold)
                    Text("• 15 gün önce")
                    Text("• 5 gün önce")
                    Text("• Son gün")
                    Text("• Saatler: 09:00 ve 15:50")
                }
            }
        }
    }
    
    if (silDialog) {
        AlertDialog(
            onDismissRequest = { silDialog = false },
            title = { Text("Emin misiniz?") },
            text = { Text("${arac.plaka} silinecek") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.aracSil(arac)
                        silDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("SİL", color = KirmiziHata)
                }
            },
            dismissButton = {
                TextButton(onClick = { silDialog = false }) {
                    Text("İPTAL")
                }
            }
        )
    }
}

@Composable
fun BilgiKart(baslik: String, deger: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(baslik)
            Text(deger, fontWeight = FontWeight.Bold)
        }
    }
}
