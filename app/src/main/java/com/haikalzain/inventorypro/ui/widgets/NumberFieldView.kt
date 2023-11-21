package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 6/01/15.
 */
class NumberFieldView(context: Context?, label: String?, isFilterView: Boolean) : FieldView(
    context!!, label!!, isFilterView
) {
    private var numberPicker: NumberPicker? = null
    private val max_value = 1000000000
    private val min_value = -1000000000
    override fun createInputView(context: Context?): View? {
        numberPicker = NumberPicker(context)
        numberPicker?.setFormatter { value -> (value + min_value).toString() }
        numberPicker?.maxValue = max_value - min_value
        numberPicker?.minValue = 0
        numberPicker?.value = -min_value
        numberPicker?.descendantFocusability = FOCUS_BLOCK_DESCENDANTS
        numberPicker?.invalidate()
        return numberPicker
    }

    public override fun setInputViewValue(dataString: String?) {
        numberPicker!!.value = dataString!!.toInt() - min_value
    }

    override val fieldType: FieldType
        get() = FieldType.NUMBER
    override val inputDataString: String
        protected get() = (numberPicker!!.value + min_value).toString()

    override fun isDialog(): Boolean {
        return true
    }
}