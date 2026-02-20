package com.sigortahatiratici.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sigortahatiratici.data.Arac
import com.sigortahatiratici.ui.theme.*
import com.sigortahatiratici.viewmodel.AracViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnaSayfa(navController: NavController) {
    val viewModel: AracViewModel = viewModel()
    val araclar by viewModel.tumAraclar.collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Sigorta Hatırlatıcı",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaviPrimary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("arac_ekle") },
                containerColor = MaviPrimary
            ) {
                Icon(Icons.Default.Add, "Ekle", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OzetKart(araclar)
            
            if (araclar.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        "Henüz araç eklenmemiş\n+ butonuna tıklayın",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(araclar) { arac ->
                        AracKart(arac) {
                            navController.navigate("arac_detay/${arac.plaka}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OzetKart(araclar: List<Arac>) {
    val bugun = LocalDate.now()
    val kritik = araclar.filter { 
        ChronoUnit.DAYS.between(bugun, it.bitisTarihi) in 0..15 
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (kritik.isNotEmpty()) KirmiziHata else YesilBasarili
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (kritik.isNotEmpty()) 
                    "⚠️ ${kritik.size} araç için yenileme yaklaşıyor!" 
                else 
                    "✅ Tüm sigortalar güncel",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AracKart(arac: Arac, onClick: () -> Unit) {
    val bugun = LocalDate.now()
    val kalan = ChronoUnit.DAYS.between(bugun, arac.bitisTarihi).toInt()
    
    val (renk, yazi) = when {
        kalan < 0 -> KirmiziHata to Color.White
        kalan <= 5 -> TuruncuUyari to Color.White
        kalan <= 15 -> MaviTertiary to MaviPrimary
        else -> MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = renk)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    arac.plaka,
                    fontWeight = FontWeight.Bold,
                    color = yazi,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    if (arac.sigortaTuru.name == "TRAFIK_SIGORTASI") "Trafik Sigortası" else "Kasko",
                    color = yazi.copy(alpha = 0.8f)
                )
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    "$kalan gün",
                    fontWeight = FontWeight.Bold,
                    color = yazi
                )
                Text(
                    arac.bitisTarihi.toString(),
                    color = yazi.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
