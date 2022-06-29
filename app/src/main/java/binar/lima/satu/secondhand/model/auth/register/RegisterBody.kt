package binar.lima.satu.secondhand.model.auth.register

import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("image")
    val image: Any,
    @SerializedName("password")
    val password: String?,
    @SerializedName("phone_number")
    val phoneNumber: Long?,
    @SerializedName("city")
    val city: String,
)
