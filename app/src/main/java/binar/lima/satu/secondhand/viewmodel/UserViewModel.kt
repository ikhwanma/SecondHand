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

    fun setToken(token : String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    fun getToken() : LiveData<String>{
        return pref.getToken().asLiveData()
    }

    fun setBiometric(token : String){
        viewModelScope.launch {
            pref.setBiometric(token)
        }
    }

    fun getBiometric() : LiveData<String>{
        return pref.getBiometric().asLiveData()
    }

    fun setCategory(category: Int){
        viewModelScope.launch {
            pref.setCategory(category)
        }
    }

    fun getCategory() : LiveData<Int>{
        return pref.getCategory().asLiveData()
    }
}