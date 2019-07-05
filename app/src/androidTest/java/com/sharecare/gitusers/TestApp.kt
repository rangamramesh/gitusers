package com.sharecare.gitusers

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.sharecare.gitusers.util.GithubTestRunner].
 */
class TestApp : Application()
