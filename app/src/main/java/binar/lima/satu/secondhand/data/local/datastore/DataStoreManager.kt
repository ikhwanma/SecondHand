package binar.lima.satu.secondhand.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context : Context) {

    suspend fun setToken(token : String){
        context.tokenDataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    fun getToken() : Flow<String>{
        return context.tokenDataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    companion object{
        private const val TOKENDATA_NAME = "token_preferences"

        private val TOKEN_KEY = stringPreferencesKey("token_key")

        private val Context.tokenDataStore by preferencesDataStore(
            name = TOKENDATA_NAME
        )
    }
}