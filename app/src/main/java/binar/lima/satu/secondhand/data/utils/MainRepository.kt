package binar.lima.satu.secondhand.data.utils

import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.seller.order.PatchOrderBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper : ApiHelper) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiHelper.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiHelper.getLoginUser(header)
    suspend fun registerUser(registerBody: RegisterBody) = apiHelper.registerUser(registerBody)

    suspend fun updateUser(
        token : String,
        fullName : RequestBody,
        address : RequestBody,
        email : RequestBody,
        phoneNumber : RequestBody,
        city : RequestBody,
        image : MultipartBody.Part,
    ) = apiHelper.updateUser(
        token, fullName, address, email, phoneNumber, city, image
    )

    //================Seller================
    suspend fun getAllCategory() = apiHelper.getAllCategory()
    suspend fun getSellerProduct(token : String) = apiHelper.getSellerProduct(token)
    suspend fun getSellerOrder(token : String, status : String?) = apiHelper.getSellerOrder(token, status)
    suspend fun getDetailSellerOrder(token : String, id: Int) = apiHelper.getDetailSellerOrder(token, id)
    suspend fun patchSellerOrder(token : String,id : Int, body: PatchOrderBody) = apiHelper.patchSellerOrder(token, id, body)
    suspend fun getSellerBanner() = apiHelper.getSellerBanner()

    suspend fun addSellerProduct(
        token: String,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        category_ids: RequestBody,
        location: RequestBody,
        image: MultipartBody.Part,
    ) = apiHelper.addSellerProduct(
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
        apiHelper.getAllProduct(status = status, category_id = category_id, search = search)
    suspend fun getProduct(id: Int) = apiHelper.getProduct(id)
    suspend fun postOrder(header: String, order: PostOrderBody) = apiHelper.postOrder(header, order)
    suspend fun getBuyerOrder(token : String) = apiHelper.getBuyerOrder(token)

    //=============Notification=============
    suspend fun getNotification(header: String) = apiHelper.getNotification(header)
    suspend fun patchNotification(header: String, id: Int) = apiHelper.patchNotification(header, id)
}