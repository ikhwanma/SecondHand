package binar.lima.satu.secondhand.data.remote

import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.login.PostLoginResponse
import binar.lima.satu.secondhand.model.auth.register.PostRegisterResponse
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.buyer.product.GetBuyerProductResponseItem
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponse
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
import binar.lima.satu.secondhand.model.seller.product.ProductBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //========================Auth===============================
    //--Login--
    @POST("/auth/login")
    suspend fun loginUser(
        @Body user : LoginBody
    ) : PostLoginResponse

    @GET("/auth/user")
    suspend fun getLoginUser(
        @Header("access_token") header : String
    ) : GetLoginResponse

    //--Register--
    @POST("/auth/register")
    suspend fun registerUser(
        @Body user : RegisterBody
    ) : PostRegisterResponse

    //--Profile--
    @PUT("/auth/user/{id}")
    suspend fun updateUser(
        @Header("access_token") header : String
    ) : PostRegisterResponse

    //=======================Seller==============================
    //--Product--
    @POST("/seller/product")
    suspend fun addSellerProduct(
        @Header("access_token") token : String,
        @Body product : ProductBody
    )

    @Multipart
    @POST("/seller/product")
    suspend fun testAddSellerProduct(
        @Header("access_token") token : String,
        @Part("name") name : RequestBody,
        @Part("description") description : RequestBody,
        @Part("base_price") base_price : RequestBody,
        @Part("category_ids") category_ids : RequestBody,
        @Part("location") location : RequestBody,
        @Part image : MultipartBody.Part,
    ) : RequestBody
    //--Category--
    @GET("/seller/category")
    suspend fun getAllCategory() : List<GetSellerCategoryResponseItem>

    //=======================Buyer==============================
    //--Product--
    @GET("/buyer/product")
    suspend fun getAllProduct(
        @Query("status") status : String?,
        @Query("category_id") category_id : Int?,
        @Query("search") search : String?
    ) : List<GetProductResponseItem>

    @GET("/buyer/product/{id}")
    suspend fun getProduct(
        @Path("id") id : Int
    ) : GetProductResponseItem


    //====================Notification===========================
    //--Product--
    @GET("/notification")
    suspend fun getNotification(
        @Header("access_token") token : String
    ) : List<GetNotificationResponseItem>

    @PATCH("/notification/{id}")
    suspend fun patchNotification(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : GetNotificationResponseItem
}