package binar.lima.satu.secondhand.data.remote

import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.login.PostLoginResponse
import binar.lima.satu.secondhand.model.auth.register.PostRegisterResponse
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    //========================Auth===============================
    //--Login--
    @POST("/auth/login")
    suspend fun loginUser(
        @Body user : LoginBody
    ) : PostLoginResponse

    @GET("/auth/user/{id}")
    suspend fun getLoginUser(
        @Header("access_token") header : String
    ) : GetLoginResponse

    //--Register--
    @POST("/auth/register")
    suspend fun registerUser(
        @Body user : RegisterBody
    ) : PostRegisterResponse

    //=======================Seller==============================
    //--Product--
    @POST("/seller/product")
    suspend fun addSellerProduct(
        @Header("access_token") token : String,
        @Body product : ProductBody
    )

}