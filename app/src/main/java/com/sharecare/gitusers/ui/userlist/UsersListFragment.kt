package com.sharecare.gitusers.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.sharecare.gitusers.AppExecutors
import com.sharecare.gitusers.R
import com.sharecare.gitusers.binding.FragmentDataBindingComponent
import com.sharecare.gitusers.databinding.FragmentUsersListBinding
import com.sharecare.gitusers.di.Injectable
import com.sharecare.gitusers.model.User
import com.sharecare.gitusers.testing.OpenForTesting
import com.sharecare.gitusers.ui.common.RetryCallback
import com.sharecare.gitusers.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

@OpenForTesting
class UsersListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentUsersListBinding>()

    var adapter by autoCleared<UsersListAdapter>()

    lateinit var usersListViewModel: UsersListViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_users_list,
                container,
                false,
                dataBindingComponent
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usersListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UsersListViewModel::class.java)
        binding.setLifecycleOwner(viewLifecycleOwner)
        initRecyclerView()
        val listAdapter = UsersListAdapter(
                dataBindingComponent = dataBindingComponent,
                appExecutors = appExecutors,
                showFullName = true
        ) { repo ->
            navController().navigate(
                    UsersListFragmentDirections.actionUsersListFragmentToUserFragment(repo.login, repo.avatarUrl)
            )
        }
        binding.query = usersListViewModel.query
        binding.userList.adapter = listAdapter
        adapter = listAdapter

        initSearchInputListener()

        binding.callback = object : RetryCallback {
            override fun retry() {
                usersListViewModel.refresh()
            }
        }
        usersListViewModel.setQuery("0")
        usersListViewModel.results.observe(this, Observer<PagedList<User>> {
            adapter.submitList(it)
        })
    }

    private fun initSearchInputListener() {
    }

    private fun initRecyclerView() {
        binding.searchResult = usersListViewModel.results
        usersListViewModel.results.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result)
        })

        usersListViewModel.loadMoreStatus.observe(viewLifecycleOwner, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                binding.loadingMore = loadingMore.isRunning
                val error = loadingMore.errorMessageIfNotHandled
                if (error != null) {
                    Snackbar.make(binding.loadMoreBar, error, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }


    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
