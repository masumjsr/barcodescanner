package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.view.View
import android.widget.DatePicker
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 21/01/15.
 */
class DateFieldView(context: Context?, label: String?, isFilterView: Boolean) :
    FieldView(context, label, isFilterView) {
    private var datePicker: DatePicker? = null
    override fun createInputView(context: Context?): View? {
        datePicker = DatePicker(context)
        datePicker?.calendarViewShown = false
        return datePicker
    }

    override val fieldType: FieldType
        get() = FieldType.DATE
    override val inputDataString: String
        get() = datePicker?.dayOfMonth.toString() + "/" + (datePicker?.month
            ?: (0 + 1)) + "/" + datePicker?.year



    override fun isDialog(): Boolean {
        return true
    }

    public override fun setInputViewValue(dataString: String?) {
        val data = dataString?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        data?.get(2)?.let {
            data[0].let { it1 ->
                datePicker!!.updateDate(
                    it.toInt(),
                    data[1].toInt() - 1, it1.toInt()
                )
            }
        }
    }


}