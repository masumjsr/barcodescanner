package com.haikalzain.inventorypro.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.common.Item
import com.haikalzain.inventorypro.databinding.FragmentSpreadBinding
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.ui.adapter.SpreadAdapter
import com.haikalzain.inventorypro.ui.dialogs.BarcodeDialog
import com.haikalzain.inventorypro.ui.dialogs.SortDialog
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.viewmodel.SpreadViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SpreadFragment : Fragment() {
    private val viewModel: SpreadViewModel by viewModel()
    var adapter: SpreadAdapter? = null


    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSpreadBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        
        

        populateField()
        return viewBinding.root
    }


    private fun populateField() {
        viewBinding.btn1.text = "Edit"
        viewBinding.btn2.text = "Sort"
        viewBinding.btn3.text = "Filter"


        viewBinding.btn3.setOnClickListener {


            val dialog = FilterFragment(viewModel)
            dialog.show(this.parentFragmentManager, "FilterFragment")
        }
        
        viewBinding.btn2.setOnClickListener {


            val spreadsheet = viewModel.spreadsheet!!
            val sortDialog = SortDialog(
                activity,
                spreadsheet.sortByOptions,
                spreadsheet.fieldSortBy,
                spreadsheet.sortIsAscending
            )
            sortDialog.setOnPositiveButtonClicked { dialog, which ->

                viewModel.sort(sortDialog.selectedField, sortDialog.isAscending)

            }
            sortDialog.show()
        }
        
        
        viewBinding.btn1.setOnClickListener {


            val dialog = BarcodeDialog(activity)
            dialog.setOnPositiveButtonClicked { d, which ->
d.dismiss()
                findNavController().navigate(SpreadFragmentDirections.actionSpreadFragmentToAddItemFragment(viewModel.fileName,true,
                   dialog.barcode

                ))
            }
            dialog.show()




        }

        viewBinding.closeSelection.setOnClickListener {
            closeMultiSelection()

        }

        viewBinding.deleteSelection.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Delete").setMessage("Are you sure you want to delete selected item?")
                .setPositiveButton("Delete"){dialog,_->
                    viewModel.deleteSelected(adapter!!.selectedItem)
                    closeMultiSelection()

                }
                .setNegativeButton("Cancel"){dialog,_->
                    dialog.dismiss()
                    closeMultiSelection()
                }
                .show()
        }



        activity?.let { activity ->
            adapter = SpreadAdapter(activity)
            adapter?.itemClickListener = object : ItemClickListener<Item> {
                override fun onItemClickListener(item: Any) {
                    val itm=item as Item
                findNavController().navigate(SpreadFragmentDirections.actionSpreadFragmentToAddItemFragment(viewModel.fileName,false,itm.getField(0).value))
                }
            }
            adapter?.itemDeleteClickListener = object : ItemClickListener<Item> {
                override fun onItemClickListener(item: Any) {
                    viewModel.delete(item as Item)
                }
            }

            adapter?.multiSelectListener= object : ItemClickListener<Boolean>{
                override fun onItemClickListener(item: Any) {
                    val selected = item as Boolean
                    showMultiSelectionUi(selected)
                }
            }
            adapter?.totalSelectListener = object : ItemClickListener<Int>{
                override fun onItemClickListener(item: Any) {
                    val total = item as Int
                    viewBinding.toolbar.findViewById<TextView>(R.id.selected_textview).setText("$total item Selected")
                }
            }

            viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
            viewBinding.recyclerView.adapter = adapter

            viewLifecycleOwner.lifecycleScope.launch {
           /*     viewModel.isComplete.collect {
                    if(it){
                        findNavController().navigate(TemplateFragmentDirections.actionTemplateFragmentToTemplateListFragment())
                    }
                }*/
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

                                adapter?.updateDataSet(viewState.data as List<Item>)

                            }
                        }
                    }
                }
            }
        }
    }

    private fun closeMultiSelection() {
        adapter?.selectedItem?.clear()
        adapter?.isMultiSelectionMode = false
        adapter?.notifyDataSetChanged()
        showMultiSelectionUi(false)
    }

    private fun showMultiSelectionUi(selected: Boolean) {
        viewBinding.toolbar.isVisible = selected
        viewBinding.btn1.isEnabled = selected.not()
        viewBinding.btn2.isEnabled = selected.not()
        viewBinding.btn3.isEnabled = selected.not()


    }


}