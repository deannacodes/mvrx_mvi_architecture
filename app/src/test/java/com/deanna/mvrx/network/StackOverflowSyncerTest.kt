package com.deanna.mvrx.network

import com.deanna.mvrx.database.UserDao
import com.deanna.mvrx.database.UserEntity
import com.deanna.mvrx.model.UserResponse
import com.deanna.mvrx.model.UsersResponse
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

class StackOverflowSyncerTest : RobolectricTest() {
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private lateinit var stackOverflowService: StackOverflowService
    private lateinit var userDao: UserDao
    private lateinit var stackOverflowSyncer: StackOverflowSyncer


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        userDao = spy(TestUserDao().create())
        stackOverflowSyncer = StackOverflowSyncer(stackOverflowService, userDao)

        whenever(stackOverflowService.getUsersRx()).thenReturn(Single.just(usersResponse))
        whenever(stackOverflowService.getUsersRxSearch("user")).thenReturn(Single.just(usersResponse))
        whenever(stackOverflowService.getUserDetail("0")).thenReturn(Single.just(usersResponse))
    }

    @Test
    fun refreshUsers() {
        stackOverflowSyncer.refreshUsers().ignoreElement().blockingAwait()

        verify(userDao, times(1)).deleteAll()
        verify(userDao, times(1)).insert(any<List<UserEntity>>())
        verify(stackOverflowService, times(1)).getUsersRx()
    }

    @Test
    fun refreshUsers_with_data() {
        userDao.insert(userEntity)

        val syncerRes = stackOverflowSyncer.refreshUsers().blockingGet()
        val daoRes = userDao.getUsersRx().blockingGet()
        Assertions.assertThat(syncerRes.size).isEqualTo(1)
        Assertions.assertThat(daoRes.size).isEqualTo(1)
        Assertions.assertThat(syncerRes[0]).isEqualTo(daoRes[0])
    }

    @Test
    fun refreshUsersSearch() {
        stackOverflowSyncer.refreshUsersSearch("user").ignoreElement().blockingAwait()

        verify(userDao, times(1)).deleteAll()
        verify(userDao, times(1)).insert(any<List<UserEntity>>())
        verify(stackOverflowService, times(1)).getUsersRxSearch("user")
    }

    @Test
    fun refreshUsersSearch_with_data() {
        userDao.insert(userEntity)

        val syncerRes = stackOverflowSyncer.refreshUsersSearch("user").blockingGet()
        val daoRes = userDao.getUsersRx().blockingGet()
        Assertions.assertThat(syncerRes.size).isEqualTo(1)
        Assertions.assertThat(daoRes.size).isEqualTo(1)
        Assertions.assertThat(syncerRes[0]).isEqualTo(daoRes[0])
    }

    private val userResponse = UserResponse(
        1,
        2,
        3,
        "userName",
        true,
        4,
        5,
        "link",
        "location",
        "profileImage",
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        "userType",
        "websiteUrl"
    )

    private val usersResponse = UsersResponse(
        false,
        listOf(userResponse),
        1,
        2
    )

    private val userEntity = UserEntity(
        55, "userName", 1, "imageUrl", "websiteUrl"
    )

}