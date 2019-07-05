package com.sharecare.gitusers.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * UserResult from a search, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class UserResult(
        val data: LiveData<PagedList<User>>,
        val networkErrors: LiveData<String>
)
