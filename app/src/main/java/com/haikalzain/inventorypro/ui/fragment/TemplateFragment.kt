package com.haikalzain.inventorypro.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.common.FieldType
import com.haikalzain.inventorypro.databinding.FragmentTemplateBinding
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.FieldItem
import com.haikalzain.inventorypro.ui.adapter.FieldAdapter
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.Mover
import com.haikalzain.inventorypro.utils.itemTouchHelper
import com.haikalzain.inventorypro.viewmodel.TemplateViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TemplateFragment : Fragment() {
    private val viewModel: TemplateViewModel by viewModel()
    var adapter: FieldAdapter? = null


    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTemplateBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewBinding.btn1.text = "Add Field..."
        viewBinding.btn2.text = "Cancel"
        viewBinding.btn3.text = "Done"

        viewBinding.btn2.setOnClickListener {
            findNavController().popBackStack()
        }

        viewBinding.btn1.setOnClickListener {
          showAddFieldDialog()
        }

        populateField()


        return viewBinding.root
    }

    private fun populateField() {


      activity?.let { activity ->
          adapter = FieldAdapter(activity)
          viewBinding.fieldRecycler.layoutManager = LinearLayoutManager(activity)
          viewBinding.fieldRecycler.adapter = adapter
          itemTouchHelper.attachToRecyclerView(viewBinding.fieldRecycler)
          adapter?.itemClickListener = object : ItemClickListener<FieldItem> {
              override fun onItemClickListener(item: Any) {
                val data = item as FieldItem
                  viewModel.removeField(data)
              }
              }
          adapter?.itemMover=object :ItemClickListener<Mover>{
              override fun onItemClickListener(item: Any) {
                  val data = item as Mover
                  viewModel.swapPosition(data.start,data.end)
              }
          }



          viewLifecycleOwner.lifecycleScope.launch {
              viewModel.isComplete.collect {
                if(it){
                    findNavController().navigate(TemplateFragmentDirections.actionTemplateFragmentToTemplateListFragment())
                }
              }
          }

          viewBinding.btn3.setOnClickListener {
              viewModel.save()
          }

          viewLifecycleOwner.lifecycleScope.launch {

              repeatOnLifecycle(Lifecycle.State.STARTED) {


                  viewModel.response.collect { viewState ->

                      when (viewState) {
                          is ApiState.Loading -> {
                              viewBinding.progressBar.isVisible =true
                          }

                          is ApiState.Failure -> {
                              viewBinding.progressBar.isVisible =false
                              Toast.makeText(activity, viewState.msg, Toast.LENGTH_SHORT).show()
                          }

                          is ApiState.Empty -> {//do something}

                          }

                          is ApiState.Success -> {
                              viewBinding.progressBar.isVisible =false
                                val data=viewState.data as List<FieldItem>
                              Log.i("123321", "populateField: $data")

                              viewBinding.fieldRecycler.adapter = adapter
                              adapter?.updateDataSet(data)

                          }
                      }
                  }
              }
          }
      }
    }


    private fun showAddFieldDialog() {
        val rootView: View = layoutInflater.inflate(R.layout.dialog_new_field, null)
        val editText = rootView.findViewById<View>(R.id.edit_text) as EditText
        val typeSpinner = rootView.findViewById<View>(R.id.spinner) as Spinner
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add New Field")
            .setView(rootView)
            .setNegativeButton(
                "Cancel"
            ) { dialog, which -> }
            .setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
                val type =
                    typeSpinner.selectedItem as FieldType
                addField(type, editText.text.toString())
            }
        val adapter: ArrayAdapter<FieldType> = ArrayAdapter<FieldType>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            FieldViewFactory.fieldTypes
        )
        typeSpinner.adapter = adapter
        val dialog = builder.create()
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var error = false
                val fieldName = editText.text.toString()
                if (fieldName == "") {
                    error = true
                    editText.error = "Cannot be blank"
                } else if (fieldName.trim { it <= ' ' } != fieldName) {
                    error = true
                    editText.error = "No leading/trailing whitespace allowed"
                } else if (/*fieldsBuilder.getFieldNames().contains(fieldName)*/ false) {
                    error = true
                    editText.error = "Field already exists"
                } else {
                    editText.error = null
                }
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !error
            }

            override fun afterTextChanged(s: Editable) {}
        })
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
    }

    private fun addField(fieldType: FieldType, name: String) {
        addFieldView(fieldType, name, true)
        viewModel.addField(FieldItem(fieldType,name,true))
       // fieldsBuilder.addFieldHeader(FieldHeader(fieldType, name))
    }

    private fun addFieldView(fieldType: FieldType, name: String, deletable: Boolean) {
        val fieldPreview = FrameLayout(requireContext())
        val rootView: View = layoutInflater.inflate(R.layout.field_preview, null)
        fieldPreview.addView(rootView)
        val fieldView = FieldViewFactory.createFieldViewForType(requireContext(), fieldType, name)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 15)
        fieldPreview.layoutParams = params
        (rootView.findViewById<View>(R.id.container) as FrameLayout).addView(fieldView)
        val deleteBtn = rootView.findViewById<View>(R.id.delete_btn) as Button
        deleteBtn.isEnabled = deletable
        deleteBtn.setOnClickListener {/* removeField(fieldType, name, fieldPreview)*/ }

        fieldView?.disableInput()
    }



}