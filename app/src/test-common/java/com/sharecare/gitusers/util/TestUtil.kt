package com.sharecare.gitusers.util

import com.sharecare.gitusers.model.User

object TestUtil {

    fun createUser(login: String) = User(
            login = login,
            avatarUrl = null,
            id = 0,
            type = null,
            reposUrl = null,
            htmlUrl = null
    )


}
