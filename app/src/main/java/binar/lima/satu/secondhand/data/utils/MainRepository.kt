package binar.lima.satu.secondhand.data.utils

import binar.lima.satu.secondhand.data.helper.ApiHelper
import binar.lima.satu.secondhand.model.auth.login.LoginBody
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper : ApiHelper) {

    //=================Auth=================
    suspend fun loginUser(loginBody: LoginBody) = apiHelper.loginUser(loginBody)
}