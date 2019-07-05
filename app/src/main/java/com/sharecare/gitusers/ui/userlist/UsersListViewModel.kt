package com.sharecare.gitusers.ui.userlist

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.sharecare.gitusers.model.UserResult
import com.sharecare.gitusers.repository.UsersRepository
import com.sharecare.gitusers.testing.OpenForTesting
import com.sharecare.gitusers.model.Resource
import com.sharecare.gitusers.model.Status
import com.sharecare.gitusers.model.User
import java.util.*
import javax.inject.Inject

@OpenForTesting
class UsersListViewModel @Inject constructor(repoRepository: UsersRepository) : ViewModel() {

    private val _id = MutableLiveData<String>()
    private val nextPageHandler = NextPageHandler(repoRepository)

    val query: LiveData<String> = _id

    private val repoResult: LiveData<UserResult> = Transformations.map(_id) {
        repoRepository.search(it)
    }
    val results: LiveData<PagedList<User>> = Transformations.switchMap(repoResult) { it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) {
        it.networkErrors
    }

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _id.value) {
            return
        }
        nextPageHandler.reset()
        _id.value = input

    }

    fun loadNextPage() {

//        setQuery(results.value!!.data!!.last().id.toString())
//        setQuery("46")
//        if(results.value!!.data == null)return
//        _id.value = results.value!!.data!!.last().id.toString()
//        _id.value?.let {
//            nextPageHandler.queryNextPage(it)
////            if (it.isNotBlank()) {
////                nextPageHandler.queryNextPage("46")
////            }
//        }
    }

    fun refresh() {
        _id.value?.let {
            _id.value = it
        }
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    class NextPageHandler(private val repository: UsersRepository) : Observer<Resource<List<User>>> {
        private var nextPageLiveData: LiveData<Resource<List<User>>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query
//            nextPageLiveData = repository.loadUsers(query, 20)
            loadMoreState.value = LoadMoreState(
                    isRunning = true,
                    errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<List<User>>?) {
            if (result == null) {
                reset()
            } else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data!!.isNotEmpty()
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = null
                                )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = result.message
                                )
                        )
                    }
                    Status.LOADING -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                    isRunning = false,
                    errorMessage = null
            )
        }
    }
}
