package binar.lima.satu.secondhand.model.history


import com.google.gson.annotations.SerializedName

data class GetHistoryResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)