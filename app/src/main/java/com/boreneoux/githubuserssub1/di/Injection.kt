package com.boreneoux.githubuserssub1.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.boreneoux.githubuserssub1.data.local.room.FavUserDatabase

class Injection(private val context: Context) {
    val database = FavUserDatabase.getDatabase(context)
    val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(name = "settings") })
}