package com.boreneoux.githubuserssub1.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boreneoux.githubuserssub1.data.local.entity.FavUserEntity
import com.boreneoux.githubuserssub1.data.local.room.FavUserDao
import com.boreneoux.githubuserssub1.data.model.DetailUserResponse
import com.boreneoux.githubuserssub1.data.model.GithubResponse
import com.boreneoux.githubuserssub1.data.model.GithubResponses
import com.boreneoux.githubuserssub1.data.remote.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainViewModel(private val dao: FavUserDao) : ViewModel() {

    private val _user = MutableLiveData<List<GithubResponse>>()
    val user: LiveData<List<GithubResponse>> = _user

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _detailUser

    private val _userFollowers = MutableLiveData<List<GithubResponse>>()
    val userFollowers: LiveData<List<GithubResponse>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<GithubResponse>>()
    val userFollowing: LiveData<List<GithubResponse>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isUserFav = MutableLiveData<Boolean>()
    val isUserFav: LiveData<Boolean> = _isUserFav

    private val _toastVal = MutableLiveData<Boolean?>()
    val toastVal: LiveData<Boolean?> = _toastVal

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    val userFav = dao.getAllFavUser()


    init {
        findUser("adam")
    }

    fun findUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponses> {
            override fun onResponse(
                call: Call<GithubResponses>,
                response: Response<GithubResponses>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _toastVal.value = true
                    _user.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponses>, t: Throwable) {
                _isLoading.value = false
                _toastVal.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun detailUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(query)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _toastVal.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun userFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<GithubResponse>> {
            override fun onResponse(
                call: Call<List<GithubResponse>>,
                response: Response<List<GithubResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun userFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<GithubResponse>> {
            override fun onResponse(
                call: Call<List<GithubResponse>>,
                response: Response<List<GithubResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun insert(user: FavUserEntity) {
        executorService.execute { dao.insert(user) }
    }

    fun delete(user: FavUserEntity) {
        executorService.execute { dao.delete(user) }
    }

    fun isUserFav(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userFav = dao.exists(username)
            _isUserFav.postValue(userFav)
        }
    }

    fun toastShown() {
        _toastVal.value = null
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}