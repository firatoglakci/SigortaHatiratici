package com.sigortahatiratici

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sigortahatiratici.data.AppDatabase
import com.sigortahatiratici.ui.screens.AnaSayfa
import com.sigortahatiratici.ui.screens.AracDetay
import com.sigortahatiratici.ui.screens.AracEkleDuzenle
import com.sigortahatiratici.ui.theme.SigortaTema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Veritabanını başlat
        AppDatabase.getDatabase(applicationContext)
        
        setContent {
            SigortaTema {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(navController = navController, startDestination = "ana_sayfa") {
                        composable("ana_sayfa") {
                            AnaSayfa(navController = navController)
                        }
                        
                        composable("arac_ekle") {
                            AracEkleDuzenle(navController = navController)
                        }
                        
                        composable(
                            "arac_ekle/{plaka}",
                            arguments = listOf(navArgument("plaka") { type = NavType.StringType })
                        ) { backStackEntry ->
                            AracEkleDuzenle(
                                navController = navController,
                                plaka = backStackEntry.arguments?.getString("plaka")
                            )
                        }
                        
                        composable(
                            "arac_detay/{plaka}",
                            arguments = listOf(navArgument("plaka") { type = NavType.StringType })
                        ) { backStackEntry ->
                            AracDetay(
                                navController = navController,
                                plaka = backStackEntry.arguments?.getString("plaka")!!
                            )
                        }
                    }
                }
            }
        }
    }
}
