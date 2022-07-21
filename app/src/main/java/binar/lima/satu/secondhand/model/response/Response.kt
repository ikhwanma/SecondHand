package binar.lima.satu.secondhand.model.response


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)