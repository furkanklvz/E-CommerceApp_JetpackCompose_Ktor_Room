package com.klavs.e_commerceapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppPref @Inject constructor(private val context: Context) {

    val Context.ds : DataStore<Preferences> by preferencesDataStore(name = "app_pref")

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    companion object {
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun setToken(token: String) {
        context.ds.edit {
            it[TOKEN] = token
        }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.ds.data.collect {
                _token.value = it[TOKEN]
            }
        }
    }

}