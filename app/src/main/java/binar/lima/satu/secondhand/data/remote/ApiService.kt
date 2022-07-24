package binar.lima.satu.secondhand.data.remote

import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.login.PostLoginResponse
import binar.lima.satu.secondhand.model.auth.register.PostRegisterResponse
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.auth.update.UpdatePasswordBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderResponse
import binar.lima.satu.secondhand.model.buyer.wishlist.GetWishlistResponseItem
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistBody
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistResponse
import binar.lima.satu.secondhand.model.history.GetHistoryResponseItem
import binar.lima.satu.secondhand.model.notification.GetNotificationResponseItem
import binar.lima.satu.secondhand.model.product.GetDetailProductResponse
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import binar.lima.satu.secondhand.model.response.Response
import binar.lima.satu.secondhand.model.seller.banner.GetSellerBannerResponseItem
import binar.lima.satu.secondhand.model.seller.order.*
import binar.lima.satu.secondhand.model.seller.product.GetSellerCategoryResponseItem
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

    @Multipart
    @PUT("/auth/user")
    suspend fun updateUser(
        @Header("access_token") token : String,
        @Part("full_name") fullName : RequestBody,
        @Part("address") address : RequestBody,
        @Part("email") email : RequestBody,
        @Part("phone_number") phoneNumber : RequestBody,
        @Part("city") city : RequestBody,
        @Part image : MultipartBody.Part?
    )

    @PUT("/auth/change-password")
    suspend fun updatePassword(
        @Header("access_token") token : String,
        @Body user: UpdatePasswordBody
    ) : Response

    //=======================Seller==============================
    //--Product--
    @GET("/seller/product")
    suspend fun getSellerProduct(
        @Header("access_token") token : String
    ) : List<GetProductResponseItem>

    @GET("/seller/banner")
    suspend fun getSellerBanner() : List<GetSellerBannerResponseItem>

    @GET("/seller/order")
    suspend fun getSellerOrder(
        @Header("access_token") token : String,
        @Query("status") status : String?
    ) : List<GetSellerOrderResponseItem>

    @GET("/seller/order/{id}")
    suspend fun getDetailSellerOrder(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : GetDetailSellerOrder

    @PATCH("/seller/order/{id}")
    suspend fun patchSellerOrder(
        @Header("access_token") token : String,
        @Path("id") id : Int,
        @Body body: PatchOrderBody
    ) : PatchSellerOrderResponse

    @Multipart
    @POST("/seller/product")
    suspend fun addSellerProduct(
        @Header("access_token") token : String,
        @Part("name") name : RequestBody,
        @Part("description") description : RequestBody,
        @Part("base_price") base_price : RequestBody,
        @Part("category_ids") category_ids : RequestBody,
        @Part("location") location : RequestBody,
        @Part image : MultipartBody.Part,
    )

    @Multipart
    @PUT("/seller/product/{id}")
    suspend fun updateSellerProduct(
        @Header("access_token") token : String,
        @Path("id") id : Int,
        @Part("name") name : RequestBody,
        @Part("description") description : RequestBody,
        @Part("base_price") base_price : RequestBody,
        @Part("category_ids") category_ids : RequestBody,
        @Part("location") location : RequestBody,
        @Part image : MultipartBody.Part?,
    )


    @DELETE("/seller/product/{id}")
    suspend fun deleteSellerProduct(
        @Header("access_token") token : String,
        @Path("id") id : Int,
    ) : Response
    //--Category--
    @GET("/seller/category")
    suspend fun getAllCategory() : List<GetSellerCategoryResponseItem>

    @GET("/seller/category/{id}")
    suspend fun getDetailCategory(
        @Path("id") id : Int
    ) : GetSellerCategoryResponseItem

    //=======================Buyer==============================
    //--Product--
    @GET("/buyer/product")
    suspend fun getAllProduct(
        @Query("status") status : String?,
        @Query("category_id") category_id : Int?,
        @Query("search") search : String?,
        @Query("page") page : Int?,
        @Query("per_page") perPage : Int?,
    ) : List<GetProductResponseItem>

    @GET("/buyer/product")
    suspend fun getAllProductPaging(
        @Query("status") status : String?,
        @Query("category_id") category_id : Int?,
        @Query("search") search : String?,
        @Query("page") page : Int?,
        @Query("per_page") perPage : Int?,
    ) : retrofit2.Response<List<GetProductResponseItem>>

    @GET("/buyer/product/{id}")
    suspend fun getProduct(
        @Path("id") id : Int
    ) : GetDetailProductResponse


    //--Order--
    @POST("/buyer/order")
    suspend fun postOrder(
        @Header("access_token") token : String,
        @Body order : PostOrderBody
    ) : PostOrderResponse

    @GET("/buyer/order")
    suspend fun getBuyerOrder(
        @Header("access_token") token : String
    ) : List<GetSellerOrderResponseItem>

    @PUT("/buyer/order/{id}")
    suspend fun updateBuyerOrder(
        @Header("access_token") token : String,
        @Path("id") id : Int,
        @Body order : PutOrderBody
    ) : PostOrderResponse

    @DELETE("/buyer/order/{id}")
    suspend fun deleteBuyerOrder(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ): Response

    //--Wishlist--
    @POST("/buyer/wishlist")
    suspend fun postBuyerWishList(
        @Header("access_token") token : String,
        @Body postWishlistBody: PostWishlistBody,
    ) : PostWishlistResponse

    @GET("/buyer/wishlist")
    suspend fun getBuyerWishList(
        @Header("access_token") token : String,
    ) : List<GetWishlistResponseItem>
    
    @DELETE("/buyer/wishlist/{id}")
    suspend fun deleteBuyerWishList(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : GetWishlistResponseItem

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

    //======================History=============================
    @GET("/history")
    suspend fun getHistory(
        @Header("access_token") token : String
    ) : List<GetHistoryResponseItem>
}