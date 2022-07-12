package binar.lima.satu.secondhand.model.seller.order

import com.google.gson.annotations.SerializedName

data class PutOrderBody(
    @SerializedName("bid_price")
    val bidPrice: Int,
)
