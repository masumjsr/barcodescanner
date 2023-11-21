package com.haikalzain.inventorypro.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haikalzain.inventorypro.common.conditions.Condition
import com.haikalzain.inventorypro.databinding.FragmentFilterBinding
import com.haikalzain.inventorypro.ui.widgets.FieldView
import com.haikalzain.inventorypro.ui.widgets.FieldViewFactory
import com.haikalzain.inventorypro.viewmodel.SpreadViewModel


class FilterFragment(val viewModel: SpreadViewModel) : BottomSheetDialogFragment() {

   // private val viewModel by lazy { requireParentFragment().getViewModel<SpreadViewModel>() }

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentFilterBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        var fieldViews: java.util.ArrayList<FieldView> = java.util.ArrayList()


        val spreadsheet = viewModel.spreadsheet!!
        val conditions = viewModel.spreadsheet!!.filterConditions
        val conditionsItems = viewModel.spreadsheet!!.filterItems
        for (header in spreadsheet.spreadsheetHeader) {
            val fieldView = FieldViewFactory.createFieldViewForType(
                requireContext(), header.type, header.name, true
            )

            viewBinding.mainLayout.addView(fieldView)
            if (fieldView != null) {
                fieldViews.add(fieldView)
            }
        }

        conditions.forEachIndexed { index, condition ->
            fieldViews[index].setSelectedFilterCondition(condition)
            fieldViews[index].setValue(conditionsItems[index])
        }

        viewBinding.btn1.setText("Cancel")
        viewBinding.btn2.setText("Done")
        viewBinding.btn1.setOnClickListener {

            this.dismiss()
        }


        viewBinding.btn2.setOnClickListener {
            val list = ArrayList<Condition>()
            val item= java.util.ArrayList<String>()

            for (fieldView in fieldViews) {
                fieldView.filterCondition?.let { it1 -> list.add(it1) }
                item.add(fieldView.dataString)

            }

            viewModel.setFilters(list,item)
            dismiss()


        }


        return viewBinding.root
    }

}