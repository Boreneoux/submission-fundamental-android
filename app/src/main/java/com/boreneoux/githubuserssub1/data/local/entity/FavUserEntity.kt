package com.boreneoux.githubuserssub1.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "fav_user")
@Parcelize
data class FavUserEntity(
    @PrimaryKey(autoGenerate = false)
    var username: String,
    var avatarUrl: String? = null,
) : Parcelable