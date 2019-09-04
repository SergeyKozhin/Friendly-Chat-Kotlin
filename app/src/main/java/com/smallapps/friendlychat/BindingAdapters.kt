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

@BindingAdapter("android:layout_height", "android:layout_width")
fun ImageView.setSize(imgHeight: Int?, imgWidth: Int?) {
    if (imgHeight != null && imgWidth != null) {
        val maxSize = 800
        val aspectRatio = imgWidth.toFloat()/imgHeight.toFloat()
        var newHeight = imgHeight
        var newWidth = imgWidth
        if (newHeight > maxSize || newWidth > maxSize) {
            if (newHeight > newWidth) {
                newHeight = maxSize
                newWidth = (newHeight * aspectRatio).toInt()
            } else {
                newWidth = maxSize
                newHeight = (newWidth / aspectRatio).toInt()
            }
        }
        val layoutParams = layoutParams
        layoutParams.height = newHeight
        layoutParams.width = newWidth
        this.layoutParams = layoutParams
    }
}