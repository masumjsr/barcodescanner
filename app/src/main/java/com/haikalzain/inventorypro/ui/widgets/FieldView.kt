package com.haikalzain.inventorypro.ui.widgets

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.common.FieldType
import com.haikalzain.inventorypro.common.conditions.Condition

/**
 * Created by haikalzain on 6/01/15.
 */
abstract class FieldView @JvmOverloads constructor(
    context: Context?,
    private val label: String?,
    isFilterView: Boolean = false
) : FrameLayout(context!!) {
    private val context: Context
    private val orientation = 0
    private val isDialog: Boolean
    private var editText: EditText? = null
    private var disabledInput: Boolean
    var isFilterView: Boolean
    private var adapter: ArrayAdapter<Condition>? = null
    private var spinner: Spinner? = null

    init {
        isDialog = isDialog()

            this.context = context!!

        disabledInput = false
        this.isFilterView = isFilterView
        if (isFilterView) {
            LayoutInflater.from(context).inflate(R.layout.field_filter_view, this)
            spinner = findViewById<View>(R.id.spinner) as Spinner
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                FieldViewFactory.getFiltersForFieldType(fieldType)
            )
            spinner!!.adapter = adapter
        } else {
            LayoutInflater.from(context).inflate(R.layout.field_view, this)
        }
        (findViewById<View>(R.id.textView) as TextView).text = getLabel()
        val layout = findViewById<View>(R.id.input_container) as FrameLayout
        if (!isDialog) {
            layout.addView(createInputView(context))
        } else {
            Log.v(TAG, "Creating dialog")
            editText = EditText(context)
            editText?.setText(defaultValue)
            editText?.setFocusable(false)
            //editText.setHint("Click to edit");
            editText?.setOnClickListener(OnClickListener {
                if (disabledInput) return@OnClickListener
                val inputView = createInputView(this@FieldView.context)
                val params = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(4, 16, 4, 4)
                inputView?.layoutParams = params
                val layout = FrameLayout(this@FieldView.context)
                layout.addView(inputView)
                //inputView.setPadding(4, 16, 4, 4);
                val builder = AlertDialog.Builder(this@FieldView.context)
                builder.setTitle(label)
                    .setView(layout)
                    .setNegativeButton("Cancel") { dialog, which -> }
                    .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            editText?.setText(inputDataString)
                        }
                    })
                builder.create().show()
                setInputViewValue(editText?.getText().toString()?:"")
            })
            layout.addView(editText)
        }
    }

    protected abstract fun createInputView(context: Context?): View?
    protected abstract fun setInputViewValue(dataString: String?)
    fun setValue(dataString: String?) {
        if (isDialog) {
            editText!!.setText(dataString)
        } else {
            setInputViewValue(dataString)
        }
    }

    fun getLabel(): String {
        return "$label:"
    }

    abstract val fieldType: FieldType?
    protected abstract val inputDataString: String
    val filterCondition: Condition?
        get() = if (!isFilterView) null else spinner!!.selectedItem as Condition

    protected open fun isDialog(): Boolean {
        return true
    }

    protected val defaultValue: String
        protected get() = FieldViewFactory.getDefaultValue(fieldType)
    val dataString: String
        get() = if (isDialog) {
            editText!!.text.toString()
        } else {
            inputDataString
        }

    fun disableInput() {
        descendantFocusability = FOCUS_BLOCK_DESCENDANTS
        disabledInput = true
    }

    fun setSelectedFilterCondition(filterCondition: Condition) {
        if (!isFilterView) {
            throw RuntimeException("This is not a FilterView")
        }
        for (i in 0 until adapter!!.count) {
            val c = adapter!!.getItem(i)
            if (c == filterCondition) {
                spinner!!.setSelection(i)
            }
        }
    }

    companion object {
        private const val TAG = "FieldView"
    }
}