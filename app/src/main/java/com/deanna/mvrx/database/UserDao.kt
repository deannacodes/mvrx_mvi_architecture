package com.deanna.mvrx.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Query("SELECT * FROM userResponses") //TODO - Sort?
    fun getUsersRx(): Single<List<UserEntity>>

    @Query("SELECT * FROM userResponses WHERE display_name LIKE '%' || :query || '%' ")
    fun getUsersRxSearch(query: String): Single<List<UserEntity>>

    @Query("SELECT * FROM userResponses WHERE id = :userId")
    fun getUserDetailRx(userId: Int): Single<UserEntity>

    @Query("DELETE FROM userResponses")
    fun deleteAll()
}