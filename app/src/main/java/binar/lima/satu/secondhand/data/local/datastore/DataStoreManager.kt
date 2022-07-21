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

    suspend fun setBiometric(token : String){
        context.biometricDataStore.edit {
            it[BIOMETRIC_KEY] = token
        }
    }

    fun getToken() : Flow<String>{
        return context.tokenDataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    fun getBiometric() : Flow<String>{
        return context.biometricDataStore.data.map {
            it[BIOMETRIC_KEY] ?: ""
        }
    }

    companion object{
        private const val TOKENDATA_NAME = "token_preferences"
        private const val BIOMETRICDATA_NAME = "biometric_preferences"

        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val BIOMETRIC_KEY = stringPreferencesKey("biometric_key")

        private val Context.tokenDataStore by preferencesDataStore(
            name = TOKENDATA_NAME
        )

        private val Context.biometricDataStore by preferencesDataStore(
            name = BIOMETRICDATA_NAME
        )
    }
}