package binar.lima.satu.secondhand.model.product


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
) : Parcelable