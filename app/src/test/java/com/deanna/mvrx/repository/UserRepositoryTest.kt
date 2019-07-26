package com.deanna.mvrx.repository

import com.deanna.mvrx.database.UserDao
import com.deanna.mvrx.database.UserEntity
import com.deanna.mvrx.model.User
import com.deanna.mvrx.network.StackOverflowService
import com.deanna.mvrx.network.StackOverflowSyncer
import com.deanna.mvrx.util.RobolectricTest
import com.deanna.mvrx.util.TestUserDao
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserRepositoryTest : RobolectricTest() {
    private lateinit var stackOverflowSyncer: StackOverflowSyncer
    private lateinit var stackOverflowService: StackOverflowService
    private lateinit var userDao: UserDao
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        userDao = spy(TestUserDao().create())
        stackOverflowService = mock()
        stackOverflowSyncer = StackOverflowSyncer(stackOverflowService,userDao)
        repository = UserRepository(stackOverflowSyncer, userDao)
        
    }

    @Test
    fun getUsersRx() {
        userDao.insert(userEntity)

        val user = repository.getUsersRx().blockingGet()

        verify(stackOverflowSyncer, times(1)).refreshUsers()
        verify(userDao, times(1)).getUsersRx()
        Assertions.assertThat(user.size).isEqualTo(1)
        Assertions.assertThat(user[0].userId == 0)
    }

    private val userEntity = UserEntity(
        0, "userName", 1, "imageUrl", "websiteUrl"
    )

    private val userModel = User(
        0, "userName", 1, "imageUrl", "websiteUrl"
    )
}