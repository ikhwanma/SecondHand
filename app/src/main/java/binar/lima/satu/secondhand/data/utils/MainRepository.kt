package binar.lima.satu.secondhand.data.utils

import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
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
    suspend fun getSellerOrder(token : String) = apiHelper.getSellerOrder(token)

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

    //=============Notification=============
    suspend fun getNotification(header: String) = apiHelper.getNotification(header)
    suspend fun patchNotification(header: String, id: Int) = apiHelper.patchNotification(header, id)
}