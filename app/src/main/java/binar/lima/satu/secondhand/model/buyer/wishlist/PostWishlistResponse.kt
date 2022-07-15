package binar.lima.satu.secondhand.model.buyer.wishlist


import com.google.gson.annotations.SerializedName

data class PostWishlistResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("Product")
    val product: Product
)