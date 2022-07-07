package binar.lima.satu.secondhand.data.helper

import binar.lima.satu.secondhand.data.remote.ApiService
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.seller.order.PatchOrderBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Part
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiService.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiService.getLoginUser(header)
    suspend fun registerUser(registerBody: RegisterBody) = apiService.registerUser(registerBody)

    suspend fun updateUser(
        token : String,
        fullName : RequestBody,
        address : RequestBody,
        email : RequestBody,
        phoneNumber : RequestBody,
        city : RequestBody,
        image : MultipartBody.Part,
    ) = apiService.updateUser(
        token, fullName, address, email, phoneNumber, city, image
    )

    //================Seller================
    suspend fun getSellerProduct(token : String) = apiService.getSellerProduct(token)
    suspend fun getSellerOrder(token : String, status : String?) = apiService.getSellerOrder(token, status)
    suspend fun getDetailSellerOrder(token : String, id: Int) = apiService.getDetailSellerOrder(token, id)
    suspend fun patchSellerOrder(token : String,id : Int, body: PatchOrderBody) = apiService.patchSellerOrder(token, id, body)
    suspend fun getSellerBanner() = apiService.getSellerBanner()

    suspend fun getAllCategory() = apiService.getAllCategory()

    suspend fun addSellerProduct(
        token: String,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        category_ids: RequestBody,
        location: RequestBody,
        image: MultipartBody.Part,
    ) = apiService.addSellerProduct(
        token,
        name,
        description,
        base_price,
        category_ids,
        location,
        image
    )

    //================Buyer================
    suspend fun getAllProduct(status: String? = null, category_id: Int? = null, search: String? = null) =
        apiService.getAllProduct(status = status, category_id = category_id, search = search)
    suspend fun getProduct(id: Int) = apiService.getProduct(id)
    suspend fun postOrder(header: String, order: PostOrderBody) = apiService.postOrder(header, order)
    suspend fun getBuyerOrder(token : String) = apiService.getBuyerOrder(token)

    //=============Notification=============
    suspend fun getNotification(header: String) = apiService.getNotification(header)
    suspend fun patchNotification(header: String, id: Int) =
        apiService.patchNotification(header, id)
}