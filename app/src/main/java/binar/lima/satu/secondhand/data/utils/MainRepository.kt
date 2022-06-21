package binar.lima.satu.secondhand.data.utils

import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper : ApiHelper) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiHelper.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiHelper.getLoginUser(header)
    suspend fun registerUser(registerBody: RegisterBody) = apiHelper.registerUser(registerBody)
    suspend fun updateUser(header: String) = apiHelper.updateUser(header)

    //================Seller================
    suspend fun addSellerProduct(token: String, product: ProductBody) = apiHelper.addSellerProduct(token, product)
    suspend fun getAllCategory() = apiHelper.getAllCategory()

    suspend fun testAddSellerProduct(
        token : String,
        name : RequestBody,
        description : RequestBody,
        base_price : RequestBody,
        category_ids : RequestBody,
        location : RequestBody,
        image :  MultipartBody.Part,
    ) = apiHelper.testAddSellerProduct(token, name, description, base_price, category_ids, location, image)

    //================Buyer================
    suspend fun getAllProduct(status : String? = "", category_id : Int? = null) = apiHelper.getAllProduct(status = status, category_id = category_id)
    suspend fun getProduct(id: Int) = apiHelper.getProduct(id)

    //=============Notification=============
    suspend fun getNotification(header: String) = apiHelper.getNotification(header)
    suspend fun patchNotification(header: String, id: Int) = apiHelper.patchNotification(header, id)
}