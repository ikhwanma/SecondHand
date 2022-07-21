package binar.lima.satu.secondhand.model.auth.update

import com.google.gson.annotations.SerializedName

data class UpdatePasswordBody(
    @SerializedName("current_password")
    val currentPassword: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("confirm_password")
    val confirmPassword: String
)
