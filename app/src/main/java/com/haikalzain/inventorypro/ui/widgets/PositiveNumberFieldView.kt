package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 7/01/15.
 */
class PositiveNumberFieldView(context: Context?, label: String?, isFilterView: Boolean) : FieldView(
    context, label, isFilterView
) {
    private var numberPicker: NumberPicker? = null
    override fun createInputView(context: Context?): View? {
        numberPicker = NumberPicker(context)
        numberPicker?.minValue = 0
        numberPicker?.maxValue = 1000000000
        numberPicker?.value = 0
        return numberPicker
    }

    override val fieldType: FieldType
        get() = FieldType.POSITIVE_NUMBER
    override val inputDataString: String
        get() = numberPicker?.value.toString()

    override fun isDialog(): Boolean {
        return true
    }

    public override fun setInputViewValue(dataString: String?) {
        try {
            numberPicker?.value = dataString?.toInt()?:0
        } catch (e: NumberFormatException) {
            numberPicker?.value = 0
        }
    }
}