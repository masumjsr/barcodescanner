package com.haikalzain.inventorypro.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.databinding.FragmentSpreadListBinding
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.TemplateItem
import com.haikalzain.inventorypro.ui.adapter.SpreadAdapter
import com.haikalzain.inventorypro.ui.adapter.SpreadListAdapter
import com.haikalzain.inventorypro.ui.adapter.TemplateAdapter
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import com.haikalzain.inventorypro.viewmodel.SpreadListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class SpreadListFragment : Fragment() {
    private val viewModel: SpreadListViewModel by viewModel()

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSpreadListBinding.inflate(layoutInflater)
    }
    var adapter: SpreadListAdapter? = null
   var template:ArrayList<TemplateItem> ?=null

    var spinnerAdapter:ArrayAdapter<String>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        activity?.let {activity->

            viewBinding.floatingActionButton.setOnClickListener {



                val layoutInflater = LayoutInflater.from(context)
               val rootView = layoutInflater.inflate(R.layout.dialog_new_spreadsheet, null)
                val  fileEdit:TextInputLayout = rootView.findViewById(R.id.editText)
                val templateSpinner = rootView.findViewById<Spinner>(R.id.spinner)
                templateSpinner.adapter=spinnerAdapter


              val  builder = AlertDialog.Builder(activity)
                builder.setTitle("New Spreadsheet")
                    .setView(rootView)
                    .setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }
                    .setPositiveButton("OK") { _: DialogInterface?, _: Int ->


                        viewModel.createFile(templateSpinner.selectedItem.toString(),fileEdit.editText?.text.toString())

                    }

              val  dialog = builder.create()
                dialog.show()

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
                fileEdit.editText?.doOnTextChanged { text, _, _, _ ->

                    val fileName: String = text.toString()
                    var error =true
                    if (fileName == "") {
                        error = true
                        fileEdit.error = "Cannot be blank"
                    } else if (!FileUtils.isFileNameValid(fileName)) {
                        error = true
                        fileEdit.error = "Invalid template name"
                    } else if (adapter!!.items.any { it.title.replace(".xls", "") == fileName }) {
                        error = true
                        fileEdit.error = "Template already exists"
                    } else {
                        fileEdit.error = null
                         error = false


                    }

                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !error

                }

            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
               findNavController().popBackStack()
            }

            adapter = SpreadListAdapter()
            adapter?.itemClickListener = object : ItemClickListener<TemplateItem> {
                override fun onItemClickListener(item: Any) {
                    val destination=SpreadListFragmentDirections.actionSpreadListFragmentToSpreadFragment((item as TemplateItem).title)
                    findNavController().navigate(destination)

                }

            }
            adapter?.itemMenuClickListener = object : ItemClickListener<TemplateItem> {
                override fun onItemClickListener(item: Any) {
                    val data =item as TemplateItem
                    inflateInventoryOptionsDialog("${data.title}.xls")
                }
            }

            viewBinding.templateRecycler.layoutManager = LinearLayoutManager(activity)
            viewBinding.templateRecycler.adapter = adapter

            viewLifecycleOwner.lifecycleScope.launch {


                repeatOnLifecycle(Lifecycle.State.CREATED) {

                    viewModel.templateList.collect { viewState ->

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

                          val data=(viewState.data as List<String>)

                                Log.i("123321", "onCreateView: $data")

                                spinnerAdapter =ArrayAdapter<String>(
                                    activity,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    data
                                )

                                if (data.isEmpty()){
                                    val builder= androidx.appcompat.app.AlertDialog.Builder(activity)
                                    builder.setTitle("No Template found")
                                    builder.setCancelable(false)
                                    builder.setMessage("Please add at least one template ")
                                    builder.setPositiveButton("Ok"

                                    ) { dialog, _ ->
                                        dialog.dismiss()
                                        findNavController().popBackStack()
                                    }
                                    builder.show()
                                }

                            }
                        }
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {


                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    viewModel.isComplete.collectLatest{ viewState ->

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

                          val data=viewState.data as String
                                viewModel.isComplete.value=ApiState.Empty
                                val destination=SpreadListFragmentDirections.actionSpreadListFragmentToSpreadFragment(data,)
                                findNavController().navigate(destination)


                            }
                        }
                    }
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {


                repeatOnLifecycle(Lifecycle.State.CREATED) {

                    viewModel.spreadList.collect { viewState ->

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
                                try {
                                    val spread=viewState.data as ArrayList<TemplateItem>

                                    viewBinding.emptyWarning.isVisible=spread.isEmpty()

                                    adapter?.updateDataSet(spread)
                                } catch (e: Exception) {

                                    Log.i("123321", "onCreateView: ${e.message}")
                                }


                            }
                        }
                    }
                }
            }
        }
        return viewBinding.root
    }


    fun inflateInventoryOptionsDialog(value: String) {
        val inflater = LayoutInflater.from(requireActivity())
        val inventoryOptionsDialog = inflater.inflate(R.layout.inventory_dialog_options, null)
        val open = inventoryOptionsDialog.findViewById<LinearLayout>(R.id.open_option)
        val share = inventoryOptionsDialog.findViewById<LinearLayout>(R.id.share_option)
        val delete = inventoryOptionsDialog.findViewById<LinearLayout>(R.id.delete_option)
        val alert = AlertDialog.Builder(requireActivity())
        alert.setView(inventoryOptionsDialog)
        alert.setTitle(getString(R.string.more_options_dialog_title))
        val dialog = alert.create()
        alert.setCancelable(true)
        open.setOnClickListener {
            dialog.dismiss()
            val file = File(
                FileUtils.getSpreadsheetsDirectory(requireActivity()),
                value
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val fileUri = FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().getPackageName() + ".provider",
                file
            )
            intent.setDataAndType(fileUri, "application/vnd.ms-excel")
            startActivity(Intent.createChooser(intent, getString(R.string.dialog_options_open)))
        }
        share.setOnClickListener { view: View? ->
            dialog.dismiss()
            val intentShareFile = Intent(Intent.ACTION_SEND)
            val file = File(
                FileUtils.getSpreadsheetsDirectory(activity),
                value
            )
            intentShareFile.type = "application/vnd.ms-excel"
            val fileUri = FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().packageName + ".provider",
                file
            )
            intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri)
            startActivity(
                Intent.createChooser(
                    intentShareFile,
                    getString(R.string.dialog_options_share)
                )
            )
        }
        delete.setOnClickListener {
            dialog.dismiss()
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Delete Spreadsheet")
                .setMessage("Are you sure you want to delete $value?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                    "Delete"
                ) { dialog, which ->
                    FileUtils.deleteSpreadsheet(
                        requireActivity(),
                        value
                    )
                    viewModel.refresh()
                }
            builder.create().show()
        }
        dialog.show()
    }

}