//package com.deanna.mvrx.repository.models
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Transformations
//import com.deanna.mvrx.database.dao.UserDao
//import com.deanna.mvrx.database.dao.UserDetailDao
//import com.deanna.mvrx.network.StackOverflowSyncer
//import com.deanna.mvrx.repository.models.UserDetailModel
//import com.deanna.mvrx.repository.models.UserModel
//import com.deanna.mvrx.ui.user_list.User
//import io.reactivex.Single
//import javax.inject.Inject
//
////interface UserRepository {
////    fun getUsers(): LiveData<List<UserModel>>
////    fun getUsersRx(): Single<List<User>>
////    fun getUserDetail(userId: String, forceRefresh: Boolean): LiveData<UserDetailModel>
////}
//
//class UserRepositoryImpl @Inject constructor(
//    private val userDao: UserDao,
//    private val userDetailDao: UserDetailDao,
//    private val stackOverflowSyncer: StackOverflowSyncer
//) {
//
//    fun getUsers(): LiveData<List<UserModel>> {
//        stackOverflowSyncer.refreshUsers()
//        return Transformations.map(userDao.getUsersStream()) { users ->
//            users.map {
//                UserModel(
//                    it.id,
//                    it.userName,
//                    it.reputation,
//                    it.imageUrl,
//                    it.websiteUrl,
//                    it.lastAccessDate
//                )
//            }
//        }
//    }
//
//    fun getUsersRx(): Single<List<User>> {
//        stackOverflowSyncer.refreshUsers()
//        return userDao.getUsersStreamRx().flatMap { userEntities ->
//            Single.just(
//                userEntities.map {
//                    User(it.id, it.userName, it.imageUrl)
//                }
//            )
//        }
//    }
//
//    fun getUsersRx(searchTerm: String): Single<List<User>> {
//        return userDao.getUsersStreamRx(searchTerm).flatMap { userEntities ->
//            if (userEntities.isNotEmpty()) {
//                Single.just(
//                    userEntities.map {
//                        User(it.id, it.userName, it.imageUrl)
//                    }
//                )
//            } else {
//                Single.just(emptyList())
//            }
//
//        }
//    }
//
//    fun getUserDetail(userId: String, forceRefresh: Boolean): LiveData<UserDetailModel> {
//        // TODO("Only fetch if user pulls to refresh or if data is outdated")
//        stackOverflowSyncer.refreshUserDetail(userId, forceRefresh)
//        return Transformations.map(userDetailDao.getUserDetailStream(userId)) { userDetailEntity ->
//            userDetailEntity?.let {
//                UserDetailModel(
//                    it.userName,
//                    it.reputation,
//                    it.imageUrl,
//                    it.websiteUrl,
//                    it.acceptRate,
//                    it.location,
//                    it.userType
//                )
//            }
//        }
//    }
//}