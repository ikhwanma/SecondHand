package binar.lima.satu.secondhand.data.remote

import binar.lima.satu.secondhand.model.auth.login.GetLoginResponse
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

    @GET("/auth/user/{id}")
    suspend fun getLoginUser(
        @Header("access_token") header : String
    ) : GetLoginResponse

    //--Register--
    @POST("/auth/register")
    suspend fun regiterUser(
        @Body user : LoginBody
    ) : PostRegisterResponse

    @GET("/auth/user/{id}")
    suspend fun getRegisterUser(
        @Header("access_token") header : String
    ) : GetRegiterResponse
}