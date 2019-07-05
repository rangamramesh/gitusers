package com.sharecare.gitusers.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.sharecare.gitusers.util.LiveDataTestUtil.getValue
import com.sharecare.gitusers.util.TestUtil
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndLoad() {
        val user = TestUtil.createUser("foo")
        db.userDao().insert(user)

        val loaded = getValue(db.userDao().findByLogin(user.login))
        assertThat(loaded.login, `is`("foo"))

        val replacement = TestUtil.createUser("foo2")
        db.userDao().insert(replacement)

        val loadedReplacement = getValue(db.userDao().findByLogin(replacement.login))
        assertThat(loadedReplacement.login, `is`("foo2"))
    }
}
