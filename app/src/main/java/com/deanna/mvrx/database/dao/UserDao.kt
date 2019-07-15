package com.deanna.mvrx.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deanna.mvrx.database.entity.UserEntity
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<UserEntity>): LongArray

    @Query("SELECT * FROM userResponses")
    fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM userResponses ORDER BY reputation DESC")
    fun getUsersStream(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM userResponses ORDER BY reputation DESC")
    fun getUsersStreamRx(): Single<List<UserEntity>>

    @Query("SELECT * FROM userResponses WHERE user_name LIKE '%' || :searchTerm || '%' ORDER BY reputation DESC")
    fun getUsersStreamRx(searchTerm: String): Single<List<UserEntity>>
}