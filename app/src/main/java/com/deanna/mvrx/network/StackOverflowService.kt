package com.deanna.mvrx.network

import com.deanna.mvrx.model.User
import com.deanna.mvrx.model.UserResponse
import com.deanna.mvrx.model.UsersResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverflowService {

    @GET("users?order=desc&sort=reputation&site=stackoverflow")
    fun getUsersRx(): Single<UsersResponse>

    @GET("users?order=desc&sort=reputation&site=stackoverflow")
    fun getUsersRxSearch(@Query("inname") query: String): Single<UsersResponse>

    @GET("users/{id}?site=stackoverflow")
    fun getUserDetail(@Path("id") id: String): Single<UsersResponse>

}
