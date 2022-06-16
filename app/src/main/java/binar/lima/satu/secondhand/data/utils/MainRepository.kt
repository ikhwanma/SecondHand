package binar.lima.satu.secondhand.data.utils

import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import binar.lima.satu.secondhand.model.auth.register.RegisterBody
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper : ApiHelper) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiHelper.loginUser(loginBody)
    suspend fun getLoginUser(header: String) = apiHelper.getLoginUser(header)
    suspend fun registerUser(registerBody: RegisterBody) = apiHelper.registerUser(registerBody)
}