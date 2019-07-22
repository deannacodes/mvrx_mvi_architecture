package com.deanna.mvrx.network

import com.deanna.mvrx.database.UserDao
import com.deanna.mvrx.database.UserEntity
import com.deanna.mvrx.model.UsersResponse
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StackOverflowSyncer @Inject constructor(
    private val stackOverflowService: StackOverflowService,
    private val userDao: UserDao
) {

    fun refreshUsers(): Single<List<UserEntity>> {
        return Completable.fromRunnable { userDao.deleteAll() }.andThen(
            stackOverflowService.getUsersRx()
                .map(UsersResponse::toUserEntities)
                .doOnSuccess {
                    userDao.insert(it)
                }
                .doOnError { TODO() }

        ).subscribeOn(Schedulers.io())
    }

    fun refreshUsersSearch(query: String): Single<List<UserEntity>> {
        return Completable.fromRunnable { userDao.deleteAll() }.andThen(
            stackOverflowService.getUsersRxSearch(query)
                .map(UsersResponse::toUserEntities)
                .doOnSuccess {
                    userDao.insert(it)
                }
                .doOnError { TODO() }
        ).subscribeOn(Schedulers.io())
    }

}