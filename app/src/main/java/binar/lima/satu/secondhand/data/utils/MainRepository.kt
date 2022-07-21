package binar.lima.satu.secondhand.data.utils

import androidx.lifecycle.LiveData
import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.data.local.room.ProductDao
import binar.lima.satu.secondhand.data.local.room.ProductEntity
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import binar.lima.satu.secondhand.model.auth.update.UpdatePasswordBody
import binar.lima.satu.secondhand.model.buyer.order.PostOrderBody
import binar.lima.satu.secondhand.model.buyer.wishlist.PostWishlistBody
import binar.lima.satu.secondhand.model.seller.order.PatchOrderBody
import binar.lima.satu.secondhand.model.seller.order.PutOrderBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val productDao: ProductDao
) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiHelper.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiHelper.getLoginUser(header)
    suspend fun registerUser(registerBody: RegisterBody) = apiHelper.registerUser(registerBody)

    suspend fun updateUser(
        token: String,
        fullName: RequestBody,
        address: RequestBody,
        email: RequestBody,
        phoneNumber: RequestBody,
        city: RequestBody,
        image: MultipartBody.Part?,
    ) = apiHelper.updateUser(
        token, fullName, address, email, phoneNumber, city, image
    )

    suspend fun updatePassword(
        token: String,
        user: UpdatePasswordBody
    ) = apiHelper.updatePassword(token, user)

    //================Seller================
    suspend fun getAllCategory() = apiHelper.getAllCategory()
    suspend fun getDetailCategory(id: Int) = apiHelper.getDetailCategory(id)
    suspend fun getSellerProduct(token: String) = apiHelper.getSellerProduct(token)
    suspend fun getSellerOrder(token: String, status: String?) =
        apiHelper.getSellerOrder(token, status)

    suspend fun getDetailSellerOrder(token: String, id: Int) =
        apiHelper.getDetailSellerOrder(token, id)

    suspend fun patchSellerOrder(token: String, id: Int, body: PatchOrderBody) =
        apiHelper.patchSellerOrder(token, id, body)

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

    suspend fun updateSellerProduct(
        token: String,
        id: Int,
        name: RequestBody,
        description: RequestBody,
        base_price: RequestBody,
        category_ids: RequestBody,
        location: RequestBody,
        image: MultipartBody.Part?,
    ) = apiHelper.updateSellerProduct(
        token,
        id,
        name,
        description,
        base_price,
        category_ids,
        location,
        image
    )

    suspend fun deleteSellerProduct(
        token : String,
        id : Int,
    ) = apiHelper.deleteSellerProduct(token, id)

    //================Buyer================
    suspend fun getAllProduct(
        status: String? = null,
        category_id: Int? = null,
        search: String? = null,
        page : Int? = null,
        perPage : Int? = null,
    ) =
        apiHelper.getAllProduct(status = status, category_id = category_id, search = search, page = page, perPage = perPage)

    suspend fun getProduct(id: Int) = apiHelper.getProduct(id)
    suspend fun postOrder(header: String, order: PostOrderBody) = apiHelper.postOrder(header, order)
    suspend fun getBuyerOrder(token: String) = apiHelper.getBuyerOrder(token)
    suspend fun updateBuyerOrder(token: String, id: Int, order: PutOrderBody) =
        apiHelper.updateBuyerOrder(token, id, order)

    suspend fun postBuyerWishList(token: String, postWishlistBody: PostWishlistBody) =
        apiHelper.postBuyerWishList(token, postWishlistBody)
    suspend fun getBuyerWishlist(token: String) = apiHelper.getBuyerWishlist(token)
    suspend fun deleteBuyerWishList(token : String, id : Int) = apiHelper.deleteBuyerWishList(token, id)

    //=============Notification=============
    suspend fun getNotification(header: String) = apiHelper.getNotification(header)
    suspend fun patchNotification(header: String, id: Int) = apiHelper.patchNotification(header, id)

    //===============History===============
    suspend fun getHistory(token : String) = apiHelper.getHistory(token)

    //=============DatabaseRoom=============
    fun addProduct(productEntity: List<ProductEntity>) = productDao.addProduct(productEntity)
    fun getProductDb(): LiveData<List<ProductEntity>> = productDao.getProduct()
    fun deleteAllProduct() = productDao.deleteAll()
}