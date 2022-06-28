package binar.lima.satu.secondhand.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import binar.lima.satu.secondhand.data.utils.MainRepository
import binar.lima.satu.secondhand.data.utils.Resource
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    //==================================Auth
    fun loginUser(loginBody: LoginBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.loginUser(loginBody)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getLoginUser(header: String) = liveData(Dispatchers.IO){
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

    fun updateUser(token : String,
                       fullName : RequestBody,
                       address : RequestBody,
                       email : RequestBody,
                       phoneNumber : RequestBody,
                       city : RequestBody,
                       image : MultipartBody.Part) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updateUser(token, fullName, address, email, phoneNumber, city, image)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    //==================================Seller

    fun getSellerProduct(token: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getSellerProduct(token)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getAllCategory() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getAllCategory()))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun addSellerProduct(token : String,
                             name : RequestBody,
                             description : RequestBody,
                             base_price : RequestBody,
                             category_ids : RequestBody,
                             location : RequestBody,
                             image :  MultipartBody.Part) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.addSellerProduct(token, name, description, base_price, category_ids, location, image)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    //==================================Buyer

    fun getAllProduct(status: String? = null, category_id: Int? = null, search:String? = null) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getAllProduct(status, category_id, search = search)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getProduct(id: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getProduct(id)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun postOrder(header: String, order: PostOrderBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.postOrder(header, order)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    //==================================Notification
    fun getNotification(header: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getNotification(header)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun patchNotification(header: String, id: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.patchNotification(header, id)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

}