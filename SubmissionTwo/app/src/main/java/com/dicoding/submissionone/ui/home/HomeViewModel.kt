package com.dicoding.submissionone.ui.home

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.dicoding.submissionone.data.UserListResponse
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.data.api.ApiConfig
import com.dicoding.submissionone.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: FragmentActivity) : ViewModel() {
    private val _listUser = MutableLiveData<List<UserResponse>>()
    val listUser: LiveData<List<UserResponse>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNoUserFound = MutableLiveData<Boolean>()
    val isNoUserFound: LiveData<Boolean> = _isNoUserFound

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        loadListUser()
    }

    fun loadListUser() {
        _isLoading.value = true
        _isNoUserFound.value = false

        val client = ApiConfig.getApiService().getAllUsers()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = response.body()
                        if (_listUser.value.isNullOrEmpty()){
                            _isNoUserFound.value = true
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun searchUsername(username: String) {
        _isLoading.value = true
        _isNoUserFound.value = false

        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUser.value = response.body()?.userListResponse
                        if (_listUser.value.isNullOrEmpty()){
                            _isNoUserFound.value = true
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun loadFavoriteListUser(viewLifecycleOwner: LifecycleOwner){
        _isLoading.value = true
        _isNoUserFound.value = false

        mFavoriteUserRepository.getAllFavoriteUser().observe(viewLifecycleOwner){ favUserList ->
            val formatedObjectList = favUserList.map { UserResponse(login = it.login, avatarUrl = it.avatarUrl.toString())}
            _listUser.value = formatedObjectList
            if (_listUser.value.isNullOrEmpty()){
                _isNoUserFound.value = true
            }
            _isLoading.value = false
        }
    }
}