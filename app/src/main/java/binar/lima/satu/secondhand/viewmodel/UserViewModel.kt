package binar.lima.satu.secondhand.viewmodel

import androidx.lifecycle.*
import binar.lima.satu.secondhand.data.local.datastore.DataStoreManager
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val pref : DataStoreManager) : ViewModel(){

    val listCategorySelected = MutableLiveData<List<GetSellerCategoryResponseItem>>()

    val booleanBiometric = MutableLiveData(false)

    fun setToken(token : String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    fun getToken() : LiveData<String>{
        return pref.getToken().asLiveData()
    }
}