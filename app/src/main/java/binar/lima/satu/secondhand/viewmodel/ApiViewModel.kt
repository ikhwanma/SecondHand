package binar.lima.satu.secondhand.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import binar.lima.satu.secondhand.data.utils.MainRepository
import binar.lima.satu.secondhand.data.utils.Resource
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun loginUser(loginBody: LoginBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.loginUser(loginBody)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getLoginUser(header : String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getLoginUser(header)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun registerUser(registerBody: RegisterBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.registerUser(registerBody)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun addSellerProduct(token: String, product: ProductBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.addSellerProduct(token, product)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }


}