package com.sharecare.gitusers.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sharecare.gitusers.model.*

/**
 * Main database description.
 */
@Database(
        entities = [
            User::class],
        version = 6,
        exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userDao(): UserDao
}
