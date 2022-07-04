package binar.lima.satu.secondhand.model.seller.banner


import com.google.gson.annotations.SerializedName

data class GetSellerBannerResponseItem(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)