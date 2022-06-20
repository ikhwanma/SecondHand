package binar.lima.satu.secondhand.model.auth.login


import com.google.gson.annotations.SerializedName

data class PostLoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)