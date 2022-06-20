package binar.lima.satu.secondhand.model.seller.product


import com.google.gson.annotations.SerializedName

data class GetSellerCategoryResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)