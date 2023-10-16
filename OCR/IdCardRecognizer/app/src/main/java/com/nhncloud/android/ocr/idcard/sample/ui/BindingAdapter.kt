package com.nhncloud.android.ocr.idcard.sample.ui

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("android:src")
fun setImageBitmap(view: ImageView, bitmap: Bitmap?) {
    bitmap?.let { view.setImageBitmap(bitmap) }
}
