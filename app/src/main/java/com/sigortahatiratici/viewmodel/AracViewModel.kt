package com.sigortahatiratici.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sigortahatiratici.data.AppDatabase
import com.sigortahatiratici.data.Arac
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AracViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).aracDao()
    val tumAraclar: Flow<List<Arac>> = dao.tumAraclariGetir()
    
    fun aracEkle(arac: Arac) {
        viewModelScope.launch { dao.aracEkle(arac) }
    }
    
    fun aracSil(arac: Arac) {
        viewModelScope.launch { dao.aracSil(arac) }
    }
}
