package com.sigortahatiratici.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sigortahatiratici.data.Arac
import com.sigortahatiratici.data.SigortaTuru
import com.sigortahatiratici.ui.theme.MaviPrimary
import com.sigortahatiratici.viewmodel.AracViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AracEkleDuzenle(navController: NavController, plaka: String? = null) {
    val viewModel: AracViewModel = viewModel()
    val context = LocalContext.current
    
    var plakaText by remember { mutableStateOf(plaka ?: "") }
    var seciliTur by remember { mutableStateOf(SigortaTuru.TRAFIK_SIGORTASI) }
    var bitisTarihi by remember { mutableStateOf(LocalDate.now().plusYears(1)) }
    var hatirlatma by remember { mutableStateOf(true) }
    
    val tarihSecici = DatePickerDialog(
        context,
        { _, yil, ay, gun -> bitisTarihi = LocalDate.of(yil, ay + 1, gun) },
        bitisTarihi.year,
        bitisTarihi.monthValue - 1,
        bitisTarihi.dayOfMonth
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (plaka == null) "Yeni Araç" else "Düzenle",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaviPrimary,
                    titleContentColor = Color.White
                )
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
            OutlinedTextField(
                value = plakaText,
                onValueChange = { plakaText = it.uppercase() },
                label = { Text("Plaka") },
                modifier = Modifier.fillMaxWidth(),
                enabled = plaka == null,
                singleLine = true
            )
            
            Text("Sigorta Türü", fontWeight = FontWeight.Bold)
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = seciliTur == SigortaTuru.TRAFIK_SIGORTASI,
                    onClick = { seciliTur = SigortaTuru.TRAFIK_SIGORTASI },
                    label = { Text("Trafik") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = seciliTur == SigortaTuru.KASKO,
                    onClick = { seciliTur = SigortaTuru.KASKO },
                    label = { Text("Kasko") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            OutlinedButton(
                onClick = { tarihSecici.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Bitiş: ${bitisTarihi.dayOfMonth}.${bitisTarihi.monthValue}.${bitisTarihi.year}")
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column {
                    Text("Hatırlatmalar", fontWeight = FontWeight.Bold)
                    Text("15 gün, 5 gün, son gün", style = MaterialTheme.typography.bodySmall)
                    Text("09:00 ve 15:50", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = hatirlatma, onCheckedChange = { hatirlatma = it })
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (plakaText.isNotBlank()) {
                        viewModel.aracEkle(
                            Arac(plakaText, seciliTur, bitisTarihi, hatirlatma)
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = plakaText.isNotBlank()
            ) {
                Text("KAYDET", fontWeight = FontWeight.Bold)
            }
        }
    }
}
