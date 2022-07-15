package binar.lima.satu.secondhand.model.buyer.wishlist

import com.google.gson.annotations.SerializedName

data class PostWishlistBody(
    @SerializedName("product_id")
    val productId: Int,
)
