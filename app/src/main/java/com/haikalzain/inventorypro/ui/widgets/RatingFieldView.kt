package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.RatingBar
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 21/01/15.
 */
class RatingFieldView(context: Context?, label: String?, isFilterView: Boolean) : FieldView(
    context,
    label,
    isFilterView
) {
    private var ratingBar: RatingBar? = null

    override fun createInputView(context: Context?): View? {
        ratingBar = RatingBar(context)
        ratingBar?.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        ratingBar?.numStars = 5
        ratingBar?.stepSize = 1.0f
        ratingBar?.rating = 0f
        val layout = FrameLayout(context!!)
        layout.addView(ratingBar)
        return layout
    }

    public override fun setInputViewValue(dataString: String?) {
        ratingBar?.rating = dataString!!.toFloat()
    }

    override val fieldType: FieldType
        get() = FieldType.RATING
    override val inputDataString: String
        get() = "" + ratingBar?.rating?.toInt()

    override fun isDialog(): Boolean {
        return true
    }
}