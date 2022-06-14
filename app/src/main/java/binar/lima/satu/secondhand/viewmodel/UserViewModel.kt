package binar.lima.satu.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import binar.lima.satu.secondhand.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val pref : DataStoreManager) : ViewModel(){

    fun setToken(token : String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    fun getToken() : LiveData<String>{
        return pref.getToken().asLiveData()
    }
}