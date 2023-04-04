package com.dicoding.submissionone.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.data.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel() : ViewModel() {
    private val _userData = MutableLiveData<UserResponse?>()
    val userData: LiveData<UserResponse?> = _userData

    private val _listFollowers = MutableLiveData<List<UserResponse>?>()
    val listFollowers: LiveData<List<UserResponse>?> = _listFollowers

    private val _listFollowing = MutableLiveData<List<UserResponse>?>()
    val listFollowing: LiveData<List<UserResponse>?> = _listFollowing

    private val _isLoadingUser = MutableLiveData<Boolean>()
    val isLoadingUser: LiveData<Boolean> = _isLoadingUser

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    private val _isLoadingFollower = MutableLiveData<Boolean>()
    val isLoadingFollower: LiveData<Boolean> = _isLoadingFollower

    private val _isNoFollowerFound = MutableLiveData<Boolean>()
    val isNoFollowerFound: LiveData<Boolean> = _isNoFollowerFound

    private val _isNoFollowingFound = MutableLiveData<Boolean>()
    val isNoFollowingFound: LiveData<Boolean> = _isNoFollowingFound

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun loadUserData(username: String = "") {
        _isLoadingUser.value = true
        _userData.value = null

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoadingUser.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userData.value = response.body()
                        _username.value = _userData.value?.login
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoadingUser.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun loadListFollower(username: String = "") {
        _isLoadingFollower.value = true
        _isNoFollowerFound.value = false
        if (_listFollowers.value != null){
            _listFollowers.value = null
        }


        val client = ApiConfig.getApiService().getListFollower(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoadingFollower.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = response.body()
                        if (_listFollowers.value.isNullOrEmpty()){
                            _isNoFollowerFound.value = true
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoadingFollower.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun loadListFollowing(username: String = "") {
        _isLoadingFollowing.value = true
        _isNoFollowingFound.value = false
        if (_listFollowing.value != null){
            _listFollowing.value = null
        }

        val client = ApiConfig.getApiService().getListFollowing(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoadingFollowing.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = response.body()
                        if (_listFollowing.value.isNullOrEmpty()){
                            _isNoFollowingFound.value = true
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}