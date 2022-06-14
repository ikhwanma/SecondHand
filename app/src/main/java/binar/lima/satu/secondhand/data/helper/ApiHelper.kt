package binar.lima.satu.secondhand.data.helper

import binar.lima.satu.secondhand.data.remote.ApiService
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService){

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiService.loginUser(loginBody)

}