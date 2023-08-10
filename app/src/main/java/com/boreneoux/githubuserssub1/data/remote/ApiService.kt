package com.boreneoux.githubuserssub1.data.remote

import com.boreneoux.githubuserssub1.data.model.DetailUserResponse
import com.boreneoux.githubuserssub1.data.model.GithubResponse
import com.boreneoux.githubuserssub1.data.model.GithubResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String
    ): Call<GithubResponses>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<GithubResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<GithubResponse>>
}