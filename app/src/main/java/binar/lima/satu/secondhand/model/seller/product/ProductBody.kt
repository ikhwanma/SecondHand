package binar.lima.satu.secondhand.model.seller.product


import android.graphics.Bitmap
import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ProductBody(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("category_ids")
    val category_ids: List<Int>,
    @SerializedName("location")
    val location: String,
    @SerializedName("image")
    val image: Any
)
