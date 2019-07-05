package com.sharecare.gitusers.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sharecare.gitusers.repository.UsersRepository
import com.sharecare.gitusers.testing.OpenForTesting
import com.sharecare.gitusers.util.AbsentLiveData
import com.sharecare.gitusers.model.Resource
import com.sharecare.gitusers.model.User
import javax.inject.Inject

@OpenForTesting
class UserViewModel
@Inject constructor(userRepository: UsersRepository) : ViewModel() {
    private val _login = MutableLiveData<String>()
    val login: LiveData<String>
        get() = _login
    val user: LiveData<Resource<User>> = Transformations
            .switchMap(_login) { login ->
                if (login == null) {
                    AbsentLiveData.create()
                } else {
                    userRepository.loadUser(login)
                }
            }

    fun setLogin(login: String?) {
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }
}
