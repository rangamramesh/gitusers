package com.sharecare.gitusers.di

import com.sharecare.gitusers.ui.user.UserFragment
import com.sharecare.gitusers.ui.userlist.UsersListFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeUsersListFragment(): UsersListFragment
}
