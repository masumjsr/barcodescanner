package com.haikalzain.inventorypro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.haikalzain.inventorypro.databinding.FragmentAddItemBinding
import com.haikalzain.inventorypro.model.FieldItem
import com.haikalzain.inventorypro.ui.widgets.FieldView
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.viewmodel.AddItemViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddItemFragment : Fragment() {
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAddItemBinding.inflate(layoutInflater)
    }
    private val viewModel: AddItemViewModel by viewModel()

    private var fieldViews: ArrayList<FieldView>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().populateData()
        fieldViews = ArrayList()
        viewBinding.btn1.setText("Cancel")
        viewBinding.btn1.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.btn2.setOnClickListener {
            findNavController().popBackStack()

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isComplete.collectLatest {
                if(it){
                    findNavController().navigate(AddItemFragmentDirections.actionAddItemFragmentToSpreadFragment(viewModel.fileName))
                }
            }
        }

        viewBinding.btn2.text = "Done"
        viewBinding.btn2.setOnClickListener {
            fieldViews?.let {
                if(it.first().dataString.isEmpty()){
                    Toast.makeText(activity, "Please fill barcode", Toast.LENGTH_SHORT).show()

                }
                else {
                    val list =ArrayList<String>()
                    for (view in fieldViews!!) {
                        list.add(view.dataString)
                    }
                    viewModel.save(list)
                }
            }
        }
        return viewBinding.root
    }

    private fun Context.populateData() {
        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {


                viewModel.response.collect { viewState ->

                     when (viewState) {
                        is ApiState.Loading -> {
                        }

                        is ApiState.Failure -> {
                            Toast.makeText(activity, viewState.msg, Toast.LENGTH_SHORT).show()
                        }

                        is ApiState.Empty -> {//do something}

                        }

                        is ApiState.Success -> {
                            val data =(viewState.data as List<FieldItem>)

                            viewBinding.mainLayout.removeAllViews()
                            fieldViews?.clear()
                            data.forEach {
                                val fieldView = FieldViewFactory.createFieldViewForType(
                                    this@populateData,
                                    it.fieldType,
                                    it.name
                                )



                                val layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )

                                layoutParams.setMargins(0, 15, 0, 0)

                                fieldView?.setValue(it.value)

                                viewBinding.mainLayout.addView(fieldView, layoutParams)
                                if (fieldView != null) {
                                    fieldViews?.add(fieldView)
                                }
                            }
                            if(viewModel.isNew.not()){
                                fieldViews?.get(0)?.disableInput()
                            }

                        }
                    }
                }
            }
        }
    }

}