package com.dicoding.submissionone.repository

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.dicoding.submissionone.data.localdatabase.FavoriteUser
import com.dicoding.submissionone.data.localdatabase.FavoriteUserDao
import com.dicoding.submissionone.data.localdatabase.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: FragmentActivity) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mFavoriteUserDao.getFavoriteUserByUsername(username)
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavoriteUser()
    fun insert(user: FavoriteUser) {
        executorService.execute {
            mFavoriteUserDao.insert(user)
        }
    }
    fun delete(user: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(user.login.toString()) }
    }
}