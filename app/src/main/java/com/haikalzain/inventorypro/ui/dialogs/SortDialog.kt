package com.haikalzain.inventorypro.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.common.FieldHeader

/**
 * Created by haikalzain on 10/01/15.
 */
class SortDialog(
    context: Context?, fields: List<FieldHeader>,
    selectedField: FieldHeader, isAscending: Boolean
) {
    private var dialog: AlertDialog? = null
    private val builder: AlertDialog.Builder
    private val rootView: View
    private val spinner: Spinner
    private val checkBox: CheckBox

    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.dialog_sort, null)
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item,
            fields
        )
        spinner = rootView.findViewById<View>(R.id.spinner) as Spinner
        spinner.adapter = adapter
        spinner.setSelection(fields.indexOf(selectedField))
        checkBox = rootView.findViewById<View>(R.id.check_box) as CheckBox
        checkBox.isChecked = !isAscending
        builder = AlertDialog.Builder(context)
        builder.setTitle("Sort")
            .setView(rootView)
            .setNegativeButton("Cancel") { dialog, which -> }
            .setPositiveButton("OK") { dialog, which -> }
    }

    val isAscending: Boolean
        get() = !checkBox.isChecked
    val selectedField: FieldHeader
        get() = spinner.selectedItem as FieldHeader

    fun show() {
        dialog = builder.create()
        dialog?.show()
    }

    fun setOnPositiveButtonClicked(acceptedListener: DialogInterface.OnClickListener?) {
        builder.setPositiveButton("OK", acceptedListener)
    }
}