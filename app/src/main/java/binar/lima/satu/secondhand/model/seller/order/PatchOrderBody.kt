package binar.lima.satu.secondhand.model.seller.order


import com.google.gson.annotations.SerializedName

data class PatchOrderBody(
    @SerializedName("status")
    val status: String
)