package com.smallapps.friendlychat.messages

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imageUrl: String?) {
    Glide.with(this.context)
        .load(imageUrl)
        .into(this)
}