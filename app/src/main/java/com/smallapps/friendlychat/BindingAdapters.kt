package com.smallapps.friendlychat

import android.text.InputFilter
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

// Setting image to ImageView
@BindingAdapter("imageUrl")
fun ImageView.bindImage(imageUrl: String?) {
    Glide.with(this.context)
        .load(imageUrl)
        .into(this)
}

// Setting edit text maximum text length
@BindingAdapter("maxLength")
fun EditText.setMaxLength(maxLength: Int?) {
    maxLength?.let {
        this.filters += arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }
}