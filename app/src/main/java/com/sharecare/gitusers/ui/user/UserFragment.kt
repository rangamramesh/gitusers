package com.sharecare.gitusers.ui.user

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.sharecare.gitusers.AppExecutors
import com.sharecare.gitusers.R
import com.sharecare.gitusers.binding.FragmentDataBindingComponent
import com.sharecare.gitusers.databinding.UserFragmentBinding
import com.sharecare.gitusers.di.Injectable
import com.sharecare.gitusers.testing.OpenForTesting
import com.sharecare.gitusers.ui.common.RetryCallback
import com.sharecare.gitusers.util.autoCleared
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import javax.inject.Inject

@OpenForTesting
class UserFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<UserFragmentBinding>()
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var userViewModel: UserViewModel
    private val params by navArgs<UserFragmentArgs>()
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<UserFragmentBinding>(
                inflater,
                R.layout.user_fragment,
                container,
                false,
                dataBindingComponent
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                userViewModel.retry()
            }
        }
        binding = dataBinding
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        // When the image is loaded, set the image request listener to start the transaction
        binding.imageRequestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
            }
        }
        // Animation Watchdog - Make sure we don't wait longer than a second for the Glide image
        handler.postDelayed(1000) {
            startPostponedEnterTransition()
        }
        postponeEnterTransition()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)
        userViewModel.setLogin(params.login)
        binding.args = params

        binding.user = userViewModel.user
        binding.setLifecycleOwner(viewLifecycleOwner)
//        binding.repoList.adapter = rvAdapter
//        this.adapter = rvAdapter
//        initRepoList()
    }


    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
