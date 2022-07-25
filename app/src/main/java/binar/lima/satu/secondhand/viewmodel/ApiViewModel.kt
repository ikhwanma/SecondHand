package binar.lima.satu.secondhand.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import binar.lima.satu.secondhand.data.local.room.HistoryEntity
import binar.lima.satu.secondhand.data.local.room.ProductEntity
import binar.lima.satu.secondhand.data.utils.MainRepository
import binar.lima.satu.secondhand.data.utils.Resource
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.auth.update.UpdatePasswordBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistBody
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.order.PatchOrderBody
import binar.lima.satu.secondhand.model.seller.order.PutOrderBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()

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
                       image : MultipartBody.Part?) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updateUser(token, fullName, address, email, phoneNumber, city, image)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun updatePassword(token: String, user: UpdatePasswordBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updatePassword(token, user)))
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

    fun getSellerBanner() = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getSellerBanner()))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getSellerOrder(token: String, status: String?) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getSellerOrder(token, status)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getDetailSellerOrder(token: String, id : Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getDetailSellerOrder(token, id)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun patchSellerOrder(token: String,id: Int, body: PatchOrderBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.patchSellerOrder(token, id, body)))
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

    fun getDetailCategory(id : Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getDetailCategory(id)))
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

    fun updateSellerProduct(token : String,
                            id: Int,
                         name : RequestBody,
                         description : RequestBody,
                         base_price : RequestBody,
                         category_ids : RequestBody,
                         location : RequestBody,
                         image :  MultipartBody.Part?) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updateSellerProduct(token, id, name, description, base_price, category_ids, location, image)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun deleteSellerProduct(token : String,
                            id: Int ) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.deleteSellerProduct(token, id)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    //==================================Buyer

    fun getAllProduct(status: String? = null, category_id: Int? = null, search:String? = null, page:Int? = null, perPage:Int? = null) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getAllProduct(status, category_id, search, page, perPage)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getAllProductPaging(idCategory: Int): LiveData<PagingData<GetProductResponseItem>>{
        return mainRepository.getAllProductPaging(idCategory).cachedIn(viewModelScope)
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

    fun getBuyerOrder(token: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getBuyerOrder(token)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun updateBuyerOrder(token: String, id: Int, order: PutOrderBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updateBuyerOrder(token, id, order)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun deleteBuyerOrder(token: String, id: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.deleteBuyerOrder(token, id)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun postBuyerWishlist(token: String, postWishlistBody: PostWishlistBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.postBuyerWishList(token, postWishlistBody)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun getBuyerWishlist(token: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getBuyerWishlist(token)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    fun deleteBuyerWishlist(token: String, id: Int) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.deleteBuyerWishList(token, id)))
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

    //==================================History
    fun getHistory(token: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getHistory(token)))
        }catch (e : Exception){
            emit(Resource.error(data = null, message = e.message ?: "Error Occured"))
        }
    }

    //==================================DatabaseProduct
    fun addProduct(productEntity: List<ProductEntity>) = viewModelScope.launch(Dispatchers.IO){
        mainRepository.addProduct(productEntity)
    }

    fun deleteAllProduct() = viewModelScope.launch(Dispatchers.IO){
        mainRepository.deleteAllProduct()
    }

    fun getProductDb() = mainRepository.getProductDb()

    fun addHistory(historyEntity: HistoryEntity) = viewModelScope.launch(Dispatchers.IO){
        mainRepository.addHistory(historyEntity)
    }

    fun getHistory() : LiveData<List<HistoryEntity>> = mainRepository.getHistory()

    fun deleteAllHistory() = viewModelScope.launch(Dispatchers.IO){
        mainRepository.deleteAllHistory()
    }
}