package com.haikalzain.inventorypro.ui.widgets

import android.R
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.haikalzain.inventorypro.common.FieldType

/**
 * Created by haikalzain on 21/01/15.
 */
class YesNoFieldView(context: Context?, label: String?, isFilterView: Boolean) : FieldView(
    context,
    label,
    isFilterView
) {
    private var spinner: Spinner? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun createInputView(context: Context?): View? {
        spinner = Spinner(context)
        adapter = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_dropdown_item, mutableListOf("Yes", "No")
        )
        spinner?.adapter = adapter
        return spinner
    }

    public override fun setInputViewValue(dataString: String?) {
        if (dataString == "Yes") {
            spinner!!.setSelection(0)
        } else {
            spinner!!.setSelection(1)
        }
    }

    override val fieldType: FieldType
        get() = FieldType.YES_NO
    override val inputDataString: String
        get() = spinner?.selectedItem as String

    override fun isDialog(): Boolean {
        return false
    }
}