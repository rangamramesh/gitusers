package com.sharecare.gitusers.db


import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

abstract class DbTest {
    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()
    private lateinit var _db: GithubDb
    val db: GithubDb
        get() = _db

    @Before
    fun initDb() {

    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        _db.close()
    }
}
