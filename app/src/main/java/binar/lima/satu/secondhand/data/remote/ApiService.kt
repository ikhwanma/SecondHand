package binar.lima.satu.secondhand.data.remote

import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.login.PostLoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    //========================Auth===============================
    //--Login--
    @POST("/auth/login")
    suspend fun loginUser(
        @Body user : LoginBody
    ) : PostLoginResponse

}