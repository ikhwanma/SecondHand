package binar.lima.satu.secondhand.model.buyer.wishlist


import com.google.gson.annotations.SerializedName

data class GetWishlistResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("Product")
    val product: ProductX,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("user_id")
    val userId: Int
)