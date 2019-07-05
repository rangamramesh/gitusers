package com.sharecare.gitusers.api

import androidx.lifecycle.LiveData
import com.sharecare.gitusers.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

fun searchRepos(
        service: GithubService,
        query: String,
        itemsPerPage: Int,
        onSuccess: (repos: List<User>) -> Unit,
        onError: (error: String) -> Unit
) {
    service.getUsers(query, itemsPerPage).enqueue(
            object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>?, t: Throwable) {
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<List<User>>?,
                        response: Response<List<User>>
                ) {
                    if (response.isSuccessful) {
                        val repos = response.body() ?: emptyList()
                        onSuccess(repos)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

interface GithubService {

    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>

    //    users?since=20&perpage=20
    @GET("users")
    fun getUsers(@Query("since") since: String, @Query("perpage") perpage: Int): Call<List<User>>

}
