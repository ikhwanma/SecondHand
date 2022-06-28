package binar.lima.satu.secondhand.model.seller.product

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val image: Uri
) : Parcelable
