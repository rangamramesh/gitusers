package com.sharecare.gitusers.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sharecare.gitusers.model.User

/**
 * Interface for database access for User related operations.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: List<User>)

    @Query("SELECT * FROM user WHERE login = :login")
    fun findByLogin(login: String): LiveData<User>

    @Query("SELECT * FROM user")
    fun load(): DataSource.Factory<Int, User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun loadUserById(id: Int): DataSource.Factory<Int, User>

}
