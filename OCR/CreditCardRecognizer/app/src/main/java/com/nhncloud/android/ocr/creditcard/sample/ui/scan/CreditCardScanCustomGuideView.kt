package com.nhncloud.android.ocr.creditcard.sample.ui.scan

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.nhncloud.android.ocr.creditcard.CreditCardDetectable
import com.nhncloud.android.ocr.creditcard.sample.R

class CreditCardScanCustomGuideView(
    context: Context, attrs: AttributeSet?
) : ConstraintLayout(context, attrs), CreditCardDetectable {
    private var mCornerLeftTop: View? = null
    private var mCornerLeftBottom: View? = null
    private var mCornerRightTop: View? = null
    private var mCornerRightBottom: View? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mCornerLeftTop = findViewById(R.id.guide_corner_left_top)
        mCornerLeftBottom = findViewById(R.id.guide_corner_left_bottom)
        mCornerRightTop = findViewById(R.id.guide_corner_right_top)
        mCornerRightBottom = findViewById(R.id.guide_corner_right_bottom)
    }

    override fun setDetected(detected: Boolean) {
        val leftTopDrawable: Drawable?
        val leftBottomDrawable: Drawable?
        val rightTopDrawable: Drawable?
        val rightBottomDrawable: Drawable?
        if (detected) {
            leftTopDrawable = getDrawable(R.drawable.scan_guide_left_top_green)
            leftBottomDrawable = getDrawable(R.drawable.scan_guide_left_bottom_green)
            rightTopDrawable = getDrawable(R.drawable.scan_guide_right_top_green)
            rightBottomDrawable = getDrawable(R.drawable.scan_guide_right_bottom_green)
        } else {
            leftTopDrawable = getDrawable(R.drawable.scan_guide_left_top_white)
            leftBottomDrawable = getDrawable(R.drawable.scan_guide_left_bottom_white)
            rightTopDrawable = getDrawable(R.drawable.scan_guide_right_top_white)
            rightBottomDrawable = getDrawable(R.drawable.scan_guide_right_bottom_white)
        }

        mCornerLeftTop?.let { it.background = leftTopDrawable }
        mCornerLeftBottom?.let { it.background = leftBottomDrawable }
        mCornerRightTop?.let { it.background = rightTopDrawable }
        mCornerRightBottom?.let { it.background = rightBottomDrawable }
    }

    private fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }
}