package com.deanna.mvrx.repository

import com.deanna.mvrx.database.UserDao
import com.deanna.mvrx.model.User
import com.deanna.mvrx.network.StackOverflowSyncer
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val stackOverflowSyncer: StackOverflowSyncer,
    private val userDao: UserDao
) {

    fun getUsersRx(): Single<List<User>> {
        return stackOverflowSyncer.refreshUsers().flatMap {
            userDao.getUsersRx().map {
                it.map { entity ->
                    User(
                        entity.userId,
                        entity.userName,
                        entity.reputation,
                        entity.imageUrl,
                        entity.websiteUrl
                    )
                }
            }
        }
    }

    fun getUserRx(id: Int): Single<User> {
        return userDao.getUserDetailRx(id).map { entity ->
            User(
                entity.userId,
                entity.userName,
                entity.reputation,
                entity.imageUrl,
                entity.websiteUrl
            )
        }
    }


    fun getUsersRxLocalSearch(query: String): Single<List<User>> {
        return userDao.getUsersRxSearch(query).map {
            it.map { entity ->
                User(
                    entity.userId,
                    entity.userName,
                    entity.reputation,
                    entity.imageUrl,
                    entity.websiteUrl
                )
            }
        }
    }

    fun getUsersRxSearch(query: String): Single<List<User>> {
        return stackOverflowSyncer.refreshUsersSearch(query).flatMap {
            userDao.getUsersRxSearch(query).map {
                it.map { entity ->
                    User(
                        entity.userId,
                        entity.userName,
                        entity.reputation,
                        entity.imageUrl,
                        entity.websiteUrl
                    )
                }
            }
        }
    }
}