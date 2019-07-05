package com.sharecare.gitusers.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.sharecare.gitusers.ui.user.UserViewModel
import com.sharecare.gitusers.ui.userlist.UsersListViewModel
import com.sharecare.gitusers.GithubViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UsersListViewModel::class)
    abstract fun bindUsersListViewModel(usersListViewModel: UsersListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: GithubViewModelFactory): ViewModelProvider.Factory
}
