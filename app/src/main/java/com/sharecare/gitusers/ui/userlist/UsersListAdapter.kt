package com.sharecare.gitusers.ui.userlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.sharecare.gitusers.AppExecutors
import com.sharecare.gitusers.R
import com.sharecare.gitusers.databinding.UsersListItemBinding
import com.sharecare.gitusers.ui.common.DataBoundListAdapter
import com.sharecare.gitusers.model.User

class UsersListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val showFullName: Boolean,
        private val repoClickCallback: ((User) -> Unit)?
) : DataBoundListAdapter<User, UsersListItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }
        }
) {

    override fun createBinding(parent: ViewGroup): UsersListItemBinding {
        val binding = DataBindingUtil.inflate<UsersListItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.users_list_item,
                parent,
                false,
                dataBindingComponent
        )
        binding.showFullName = showFullName
        binding.root.setOnClickListener {
            binding.user?.let {
                repoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: UsersListItemBinding, item: User) {
        binding.user = item
    }
}
