package com.smallapps.friendlychat.messages

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.smallapps.friendlychat.R
import com.smallapps.friendlychat.database.FriendlyMessageDomain
import com.smallapps.friendlychat.databinding.ItemMessageBinding

// RecyclerView Adapter for list of messages
class MessageAdapter :
    ListAdapter<FriendlyMessageDomain, MessageAdapter.MessageViewHolder>(DiffCallBack) {

    // One item layout inflation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder.from(parent)
    }

    // Binding data to one item
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ViewHolder class for operations with one message
    class MessageViewHolder private constructor(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MessageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemMessageBinding.inflate(inflater, parent, false)
                return MessageViewHolder(binding)
            }
        }

        fun bind(item: FriendlyMessageDomain) {
            binding.message = item
            binding.executePendingBindings()
        }

    }

    // DiffCallback singleton for checking difference between items
    private object DiffCallBack : DiffUtil.ItemCallback<FriendlyMessageDomain>() {
        override fun areItemsTheSame(
            oldItem: FriendlyMessageDomain,
            newItem: FriendlyMessageDomain
        ): Boolean {
            return newItem === oldItem
        }

        override fun areContentsTheSame(
            oldItem: FriendlyMessageDomain,
            newItem: FriendlyMessageDomain
        ): Boolean {
            return newItem == oldItem
        }

    }
}