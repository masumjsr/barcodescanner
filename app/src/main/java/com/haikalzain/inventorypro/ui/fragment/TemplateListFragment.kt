package com.haikalzain.inventorypro.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.haikalzain.inventorypro.R
import com.haikalzain.inventorypro.databinding.FragmentTemplateListBinding
import com.haikalzain.inventorypro.iface.ItemClickListener
import com.haikalzain.inventorypro.model.TemplateItem
import com.haikalzain.inventorypro.ui.adapter.TemplateAdapter
import com.haikalzain.inventorypro.utils.ApiState
import com.haikalzain.inventorypro.utils.FileUtils
import com.haikalzain.inventorypro.viewmodel.TemplateListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TemplateListFragment : Fragment() {
    private val viewModel: TemplateListViewModel by viewModel()

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentTemplateListBinding.inflate(layoutInflater)
    }
    var adapter: TemplateAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        activity?.let {activity->

            viewBinding.floatingActionButton.setOnClickListener {



                val layoutInflater = LayoutInflater.from(context)
               val rootView = layoutInflater.inflate(R.layout.dialog_new_template, null)
                val  fileEdit:TextInputLayout = rootView.findViewById(R.id.editText)
              val  builder = AlertDialog.Builder(activity)
                builder.setTitle("New Template")
                    .setView(rootView)
                    .setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }
                    .setPositiveButton("OK") { _: DialogInterface?, _: Int ->

                        val destination=TemplateListFragmentDirections.actionTemplateListFragmentToTemplateFragment(fileEdit.editText?.text.toString(),true)
                       findNavController().navigate(destination)

                    }

              val  dialog = builder.create()
                dialog.show()

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
                fileEdit.editText?.doOnTextChanged { text, _, _, _ ->

                    val fileName: String = text.toString()
                    val error: Boolean
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

            adapter = TemplateAdapter()
            adapter?.itemClickListener = object : ItemClickListener<TemplateItem> {
                override fun onItemClickListener(item: Any) {
                    val destination =
                        TemplateListFragmentDirections.actionTemplateListFragmentToTemplateFragment(
                            (item as TemplateItem).title,
                            false
                        )
                    findNavController().navigate(destination)

                }
            }

            adapter?.itemMenuClickListener = object : ItemClickListener<TemplateItem> {


                override fun onItemClickListener(item: Any) {
                val data = (item as TemplateItem)


                    val builder = android.app.AlertDialog.Builder(activity)
                    builder.setTitle("Delete Template")
                        .setMessage("Are you sure you want to delete " + data.title + "?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete") { dialog, which ->

                           viewModel.delete(data.title)
                        }
                    builder.create().show()


                }
            }


            viewBinding.templateRecycler.layoutManager = LinearLayoutManager(activity)
            viewBinding.templateRecycler.adapter = adapter

            viewLifecycleOwner.lifecycleScope.launch {

                repeatOnLifecycle(Lifecycle.State.CREATED) {

                    viewModel.spreadList.collect { viewState ->

                        Log.i("123321", "onCreateView: $viewState")
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
                                val data=viewState.data as List<TemplateItem>
                                viewBinding.emptyWarning.isVisible=data.isEmpty()
                                adapter?.updateDataSet(data)

                            }
                        }
                    }
                }
            }
        }
        return viewBinding.root
    }

}