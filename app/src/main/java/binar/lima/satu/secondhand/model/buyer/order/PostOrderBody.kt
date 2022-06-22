package binar.lima.satu.secondhand.model.buyer.order


import com.google.gson.annotations.SerializedName

data class PostOrderBody(
    @SerializedName("bid_price")
    val bidPrice: Int,
    @SerializedName("product_id")
    val productId: Int
)