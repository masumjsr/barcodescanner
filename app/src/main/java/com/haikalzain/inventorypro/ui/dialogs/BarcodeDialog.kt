package com.haikalzain.inventorypro.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.haikalzain.inventorypro.R

/**
 * Created by haikalzain on 13/01/15.
 */
class BarcodeDialog(context: Context?) {
    private var dialog: AlertDialog? = null
    private val builder: AlertDialog.Builder
    private val fileEdit: TextInputLayout
    private val rootView: View

    init {
        val inflater = LayoutInflater.from(context)
        rootView = inflater.inflate(R.layout.dialog_new_template, null)
        builder = AlertDialog.Builder(context)
        builder.setTitle("Lookup/Add Barcode")
            .setView(rootView)
            .setMessage("Leave blank if your item doesn't have a barcode")
            .setNegativeButton("Cancel") { dialog, which -> }
            .setPositiveButton("OK") { dialog, which -> }
        fileEdit = rootView.findViewById(R.id.editText)
        fileEdit.hint = "Barcode"
        /* fileEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showErrors();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    fun setOnPositiveButtonClicked(acceptedListener: DialogInterface.OnClickListener?) {
        builder.setPositiveButton("OK", acceptedListener)
    }

    fun show() {
        dialog = builder.create()
        dialog?.show()
    }

    val barcode: String
        get() = fileEdit.editText!!.text.toString()

}