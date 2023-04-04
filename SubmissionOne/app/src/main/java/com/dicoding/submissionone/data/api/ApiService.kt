package com.dicoding.submissionone.data.api

import com.dicoding.submissionone.data.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getAllUsers() : Call<List<UserResponse>>

    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ) : Call<UserListResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ) : Call<UserResponse>

    @GET("users/{username}/followers")
    fun getListFollower(
        @Path("username") username: String
    ) : Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getListFollowing(
        @Path("username") username: String
    ) : Call<List<UserResponse>>
}