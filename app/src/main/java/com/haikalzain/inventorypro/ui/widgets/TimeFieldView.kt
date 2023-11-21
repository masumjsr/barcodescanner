package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.view.View
import android.widget.TimePicker
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 21/01/15.
 */
class TimeFieldView(context: Context?, label: String?, isFilterView: Boolean) :
    FieldView(context, label, isFilterView) {
    private var timePicker: TimePicker? = null
    override fun createInputView(context: Context?): View? {
        timePicker = TimePicker(context)
        timePicker!!.setIs24HourView(true)
        return timePicker
    }

    override val fieldType: FieldType
        get() = FieldType.TIME
    override val inputDataString: String
        get() = String.format(
            "%02d:%02d",
            timePicker?.currentHour,
            timePicker?.currentMinute
        )

    override fun isDialog(): Boolean {
        return true
    }

    public override fun setInputViewValue(dataString: String?) {
        val data = dataString!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        timePicker?.currentHour = data[0].toInt()
        timePicker?.currentMinute = data[1].toInt()
    }
}