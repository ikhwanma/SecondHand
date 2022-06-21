package binar.lima.satu.secondhand.data.helper

import binar.lima.satu.secondhand.data.remote.ApiService
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiService.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiService.getLoginUser(header)

    suspend fun registerUser(registerBody: RegisterBody) = apiService.registerUser(registerBody)
    suspend fun updateUser(header: String) = apiService.updateUser(header)

    //================Seller================
    suspend fun addSellerProduct(token: String, product: ProductBody) =
        apiService.addSellerProduct(token, product)

    suspend fun getAllCategory() = apiService.getAllCategory()

    suspend fun testAddSellerProduct(
        token: String,
        name: String,
        description: String,
        base_price: Int,
        category_ids: List<Int>,
        location: String,
        image: MultipartBody.Part,
    ) = apiService.testAddSellerProduct(
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

    //=============Notification=============
    suspend fun getNotification(header: String) = apiService.getNotification(header)
    suspend fun patchNotification(header: String, id: Int) =
        apiService.patchNotification(header, id)
}