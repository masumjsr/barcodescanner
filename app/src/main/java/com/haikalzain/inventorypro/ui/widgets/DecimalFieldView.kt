package com.haikalzain.inventorypro.ui.widgets

import android.content.Context
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.EditText
import com.haikalzain.inventorypro.common.FieldType
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.regex.Pattern

/**
 * Created by haikalzain on 21/01/15.
 */
class DecimalFieldView(context: Context?, label: String?, isFilterView: Boolean) : FieldView(
    context,
    label,
    isFilterView
) {
    private var editText: EditText? = null

    override fun createInputView(context: Context?): View? {
        editText = EditText(context)
        val d = DecimalFormatSymbols.getInstance(Locale.getDefault())
        val pattern = Pattern.compile("[0-9]*(\\.[0-9]*)?")
        editText!!.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (!pattern.matcher(dest).matches()) {
                ""
            } else null
        })
        editText!!.keyListener = DigitsKeyListener.getInstance("0123456789" + d.decimalSeparator)
        return editText
    }

    public override fun setInputViewValue(dataString: String?) {
        editText!!.setText(String.format("%01f", dataString?.toDouble()))
    }




    override val fieldType: FieldType
        get() =FieldType.DECIMAL
    override val inputDataString: String
        get() = String.format("%01f", if(editText?.text.toString()=="")defaultValue else editText?.text.toString().toDouble())
}