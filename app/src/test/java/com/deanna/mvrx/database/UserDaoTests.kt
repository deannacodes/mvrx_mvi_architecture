package com.deanna.mvrx.database


import com.deanna.mvrx.database.UserDao
import com.deanna.mvrx.database.UserEntity
import com.deanna.mvrx.util.RobolectricTest
import com.deanna.mvrx.util.TestUserDao
import org.junit.Before
import org.junit.Test

class UserDaoTests : RobolectricTest() {

    private lateinit var userDao: UserDao
    private val imgUrl: String = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
    private val webUrl: String = "https://www.google.com"

    @Before
    fun setup() {
        userDao = TestUserDao().create()
    }

    @Test
    fun inserts_user_entity() {

        val user = UserEntity(0, "userName", 100, imgUrl, webUrl)
        userDao.insert(user)

        val res = userDao.getUsersRx().blockingGet()

        assert(res.size == 1)
        val userRes = res[0]
        assert(userRes.userId == 0)
        assert(userRes.userName == "userName")
        assert(userRes.reputation == 100)
        assert(userRes.imageUrl == imgUrl)
        assert(userRes.websiteUrl == webUrl)

    }

    @Test
    fun inserts_user_entities() {

        val users = listOf(
            UserEntity(0, "userName", 100, imgUrl, webUrl),
            UserEntity(11, "userName1", 101, imgUrl + "1", webUrl + "1"),
            UserEntity(222, "userName2", 102, imgUrl + "1", webUrl + "2")
        )
        userDao.insert(users)

        val res = userDao.getUsersRx().blockingGet()

        assert(res.size == 3)
        assert(res[0].userId == 0)
        assert(res[1].userId == 11)
        assert(res[2].userId == 222)

    }

    @Test
    fun gets_matching_entries() {

        val users = listOf(
            UserEntity(0, "userName", 100, imgUrl, webUrl),
            UserEntity(11, "jon", 101, imgUrl + "1", webUrl + "1"),
            UserEntity(222, "jonSkeet", 102, imgUrl + "2", webUrl + "2")
        )
        userDao.insert(users)

        val res = userDao.getUsersRxSearch("jon").blockingGet()

        assert(res.size == 2)
        assert(res[0].userId == 11)
        assert(res[1].userId == 222)
        assert(!res.contains(UserEntity(0, "userName", 100, imgUrl, webUrl)))

    }

    @Test
    fun gets_user_by_id() {

        val users = listOf(
            UserEntity(0, "userName", 100, imgUrl, webUrl),
            UserEntity(11, "jon", 101, imgUrl + "1", webUrl + "1"),
            UserEntity(222, "jonSkeet", 102, imgUrl + "2", webUrl + "2")
        )
        userDao.insert(users)

        val res = userDao.getUserDetailRx(11).blockingGet()

        assert(res.userId == 11)
        assert(res.userName == "jon")
        assert(res.reputation == 101)
        assert(res.imageUrl == imgUrl + "1")
        assert(res.websiteUrl == webUrl + "1")

    }

    @Test
    fun deletes_all_entries() {

        val users = listOf(
            UserEntity(0, "userName", 100, imgUrl, webUrl),
            UserEntity(11, "userName1", 101, imgUrl + "1", webUrl + "1"),
            UserEntity(222, "userName2", 102, imgUrl + "2", webUrl + "2")
        )
        userDao.insert(users)

        userDao.deleteAll()
        val res = userDao.getUsersRx().blockingGet()

        assert(res.isEmpty())

    }

}