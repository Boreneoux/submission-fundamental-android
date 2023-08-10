package com.boreneoux.githubuserssub1.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boreneoux.githubuserssub1.data.local.entity.FavUserEntity

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUserEntity: FavUserEntity)

    @Delete
    fun delete(favUserEntity: FavUserEntity)

    @Query("SELECT * from fav_user ORDER BY username ASC")
    fun getAllFavUser(): LiveData<List<FavUserEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM fav_user WHERE username = :username)")
    fun exists(username: String): Boolean
}
