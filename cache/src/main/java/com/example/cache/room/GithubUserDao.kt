package com.example.cache.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cache.models.GithubUsersCacheModel
import com.example.cache.models.GithubUsersCacheModel.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow


@Dao
interface GithubUserDao {

    @Query("select * from $TABLE_NAME where isFavorite")
    fun getUsersFromCache(): Flow<List<GithubUsersCacheModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun favoriteUser(user: GithubUsersCacheModel)


}