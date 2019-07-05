package com.sharecare.gitusers.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import com.sharecare.gitusers.AppExecutors
import com.sharecare.gitusers.api.GithubService
import com.sharecare.gitusers.db.GithubDb
import com.sharecare.gitusers.db.UserDao
import com.sharecare.gitusers.model.UserResult
import com.sharecare.gitusers.testing.OpenForTesting
import com.sharecare.gitusers.model.User
import com.sharecare.gitusers.model.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UsersRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val userDao: UserDao,
        private val githubService: GithubService
) {

    //Todo: need to use NetworkBoundResource
    fun search(query: String): UserResult {

        // Get data source factory from the local cache
        val dataSourceFactory = userDao.load()

        // Construct the boundary callback
        val boundaryCallback = UsersBoundaryCallback(query, githubService, userDao)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return UserResult(data, networkErrors)
    }

    fun loadUser(login: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByLogin(
                login= login // need to replace with id
            )

            override fun createCall() = githubService.getUser(
                login = login
            )
        }.asLiveData()
    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}
