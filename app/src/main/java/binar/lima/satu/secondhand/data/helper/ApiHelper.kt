package binar.lima.satu.secondhand.data.helper

import binar.lima.satu.secondhand.data.remote.ApiService
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService){

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiService.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiService.getLoginUser(header)
    suspend fun registerUser(registerBody : RegisterBody) = apiService.registerUser(registerBody)
    suspend fun updateUser(header: String) = apiService.updateUser(header)

    //================Seller================
    suspend fun addSellerProduct(token: String, product: ProductBody) = apiService.addSellerProduct(token, product)
}