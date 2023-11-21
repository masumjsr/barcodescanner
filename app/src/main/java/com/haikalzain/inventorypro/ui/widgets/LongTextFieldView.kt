package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.view.View
import android.widget.EditText
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 6/01/15.
 */
class LongTextFieldView(context: Context?, label: String?, isFilterView: Boolean) : FieldView(
    context!!, label!!, isFilterView
) {
    private var editText: EditText? = null
    override fun createInputView(context: Context?): View? {
        editText = EditText(context)
        editText!!.setLines(4)
        return editText
    }

    public override fun setInputViewValue(dataString: String?) {
        editText!!.setText(dataString)
    }

    override val fieldType: FieldType
        get() = FieldType.LONG_TEXT
    override val inputDataString: String
        get() = editText!!.text.toString()
}