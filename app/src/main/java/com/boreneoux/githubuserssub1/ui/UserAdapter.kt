package com.boreneoux.githubuserssub1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boreneoux.githubuserssub1.data.model.GithubResponse
import com.boreneoux.githubuserssub1.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UserAdapter(private val onClick: (String?) -> Unit) :
    ListAdapter<GithubResponse, UserAdapter.UserViewHolder>(UserDiffCallback) {

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GithubResponse, onClick: (String?) -> Unit) {
            binding.apply {
                itemUserContainer.setOnClickListener { onClick(item.login) }

                tvItem.text = item.login
                Glide.with(root.context)
                    .load(item.avatarUrl)
                    .into(userImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user, onClick)
    }

}

object UserDiffCallback : DiffUtil.ItemCallback<GithubResponse>() {
    override fun areItemsTheSame(oldItem: GithubResponse, newItem: GithubResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GithubResponse, newItem: GithubResponse): Boolean {
        return oldItem == newItem
    }

}