package com.nhncloud.android.ocr.creditcard.sample.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("android:src")
fun setImageBitmap(view: ImageView, bitmap: Bitmap?) {
    bitmap?.let { view.setImageBitmap(bitmap) }
}

@BindingAdapter("drawableTopCompat")
fun setDrawableTop(view: TextView, drawable: Drawable?) {
    view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}
