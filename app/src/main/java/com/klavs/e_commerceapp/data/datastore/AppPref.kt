package com.klavs.e_commerceapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject

class AppPref @Inject constructor(private val context: Context) {

    val Context.ds : DataStore<Preferences> by preferencesDataStore(name = "app_pref")

    companion object {
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun setToken(token: String) {
        context.ds.edit {
            it[TOKEN] = token
        }
    }

    fun getToken() = context.ds.data

}