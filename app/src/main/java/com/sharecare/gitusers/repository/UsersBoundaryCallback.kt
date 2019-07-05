package com.sharecare.gitusers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.sharecare.gitusers.api.GithubService
import com.sharecare.gitusers.api.searchRepos
import com.sharecare.gitusers.db.UserDao
import com.sharecare.gitusers.model.User
import java.util.concurrent.Executors

class UsersBoundaryCallback (
        private val query: String,
        private val service: GithubService,
        private val cache: UserDao
) : PagedList.BoundaryCallback<User>() {

    // keep the last requested page.
// When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: User) {
        requestAndSaveData(itemAtEnd.id.toString())
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, NETWORK_PAGE_SIZE, { repos ->
            Executors.newSingleThreadExecutor().execute {
                cache.insert(repos)
//            {
//                lastRequestedPage++
//                isRequestInProgress = false
//            }
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
        isRequestInProgress = false
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}