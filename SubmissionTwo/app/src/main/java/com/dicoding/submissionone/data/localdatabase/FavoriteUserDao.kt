package com.dicoding.submissionone.data.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE FROM favoriteUser WHERE login = :username")
    fun delete(username: String)

    @Query("SELECT * FROM favoriteUser WHERE login = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Query("SELECT * FROM favoriteUser ORDER BY login ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>
}